package com.maru.todayroute.ui.initial

import android.net.Uri
import android.os.Build.VERSION_CODES.P
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.template.model.Link
import com.kakao.sdk.template.model.TextTemplate
import com.kakao.sdk.user.UserApiClient
import com.maru.data.model.Gender
import com.maru.data.network.request.SignUpRequest
import com.maru.data.network.Token
import com.maru.data.repository.UserRepository
import com.maru.data.repository.TokenRepository
import com.maru.todayroute.R
import com.maru.todayroute.SignInTokenInfo.token
import com.maru.todayroute.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

@HiltViewModel
class InitialViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val tokenRepository: TokenRepository
) : ViewModel() {

    private val calendar: Calendar = GregorianCalendar()
    private var year = calendar.get(Calendar.YEAR)
    private var month = calendar.get(Calendar.MONTH)
    private var date = calendar.get(Calendar.DATE)

    private var id = -1
    private lateinit var key: String
    private var gender: Gender = Gender.M
    private lateinit var email: String
    private lateinit var profileUrl: String
    private lateinit var nickname: String

    val code get() = _code
    private lateinit var _code: String
    val inviteCode: LiveData<String> get() = _inviteCode
    private var _inviteCode: MutableLiveData<String> = MutableLiveData("")

    private val _showDatePickerDialog = SingleLiveEvent<Triple<Int, Int, Int>>()
    val showDatePickerDialog: LiveData<Triple<Int, Int, Int>> get() = _showDatePickerDialog
    private val _showToastMessage = SingleLiveEvent<Int>()
    val showToastMessage: LiveData<Int> get() = _showToastMessage
    private val _moveToConnectCoupleFragment = SingleLiveEvent<Unit>()
    val moveToConnectCoupleFragment: LiveData<Unit> get() = _moveToConnectCoupleFragment
    private val _moveToInitialUserInfoFragment = SingleLiveEvent<Unit>()
    val moveToInitialUserInfoFragment: LiveData<Unit> get() = _moveToInitialUserInfoFragment
    private val _moveToMainActivity = SingleLiveEvent<Unit>()
    val moveToMainActivity: LiveData<Unit> get() = _moveToMainActivity

    init {
        if (AuthApiClient.instance.hasToken()) {
            setUserInfoFromKakaoApi()
        }
    }

    fun setUserInfoFromKakaoApi() {
        with(UserApiClient.instance) {
            me { user, error ->
                if (error == null && user != null) {
                    email = user.kakaoAccount?.email ?: ""
                    nickname = user.kakaoAccount?.profile?.nickname ?: ""
                    profileUrl = user.kakaoAccount?.profile?.thumbnailImageUrl ?: ""
                    Log.e("HI", "$email $nickname $profileUrl")
                }
            }
            accessTokenInfo { tokenInfo, error ->
                if (error == null && tokenInfo != null) {
                    key = tokenInfo.id.toString()
                    Log.d("key!!", key)
                    viewModelScope.launch {
                        trySignIn()
                    }
                }
            }
        }
    }

    private suspend fun trySignIn() {
        val result = withContext(viewModelScope.coroutineContext) {
            userRepository.signInUser(key)
        }

        if (result.isSuccess) {
            token = result.getOrNull()!!
            withContext(Dispatchers.IO) {
                tokenRepository.saveTokens(token)
            }
            getSignInCoupleInfoByAccessToken()
        } else {
            _moveToInitialUserInfoFragment.call()
        }
    }

    private suspend fun getSignInCoupleInfoByAccessToken() {
        val result = withContext(viewModelScope.coroutineContext) {
            userRepository.getMyCoupleData()
        }

        if (result.isSuccess) {
            _moveToMainActivity.call()
        } else {
            setSignInUser()
            _moveToConnectCoupleFragment.call()
        }
    }

    private suspend fun setSignInUser() {
        val result = withContext(viewModelScope.coroutineContext) {
            userRepository.getMyUserData()
        }

        if (result.isSuccess) {
            val user = result.getOrNull()!!
            id = user.id
            _code = user.code
        }
    }

    fun setUserGender(gender: Gender) {
        this.gender = gender
    }

    fun editDateButtonClicked() {
        val day = Triple(year, month, date)
        _showDatePickerDialog.value = day
    }

    fun initDate() {
        this.year = calendar.get(Calendar.YEAR)
        this.month = calendar.get(Calendar.MONTH)
        this.date = calendar.get(Calendar.DATE)
    }

    fun setDate(year: Int, month: Int, date: Int) {
        this.year = year; this.month = month; this.date = date
    }

    fun setInviteCode(inviteCode: String) {
        this._inviteCode.value = inviteCode
    }

    fun registerNewUser(birthday: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = userRepository.registerNewUser(
                SignUpRequest(
                    key,
                    gender,
                    email,
                    nickname,
                    profileUrl,
                    birthday
                )
            )

            if (result.isSuccess) {
                val accessToken = result.getOrNull()!!.access
                val refreshToken = result.getOrNull()!!.refresh
                token = Token(accessToken, refreshToken)
                viewModelScope.launch {
                    tokenRepository.saveTokens(token)
                }
                setSignInUser()
                withContext(Dispatchers.Main) {
                    _moveToConnectCoupleFragment.call()
                }
            } else {
                _showToastMessage.postValue(R.string.warning_input_birthday)
            }
        }
    }

    fun makeInviteTextTemplate(text: String, inviteLink: Uri, buttonTitle: String): TextTemplate =
        TextTemplate(
            text = nickname + text + code,
            link = Link(mobileWebUrl = inviteLink.toString()),
            buttonTitle = buttonTitle
        )

    fun tryStart() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = userRepository.getMyCoupleData()

            withContext(Dispatchers.Main) {
                if (result.isSuccess) {
                    _moveToMainActivity.call()
                } else {
                    _showToastMessage.postValue(R.string.warning_no_connected_couple_yet)
                }
            }
        }
    }

    fun connectCoupleByCode(startDate: String) {
        if (startDate.isEmpty()) {
            _showToastMessage.value = R.string.warning_input_start_date
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            val inviteCode = _inviteCode.value ?: ""
            val result = userRepository.registerNewCouple(inviteCode, startDate)

            withContext(Dispatchers.Main) {
                if (result.isSuccess) {
                    _moveToMainActivity.call()
                } else {
                    _showToastMessage.postValue(R.string.warning_check_invite_code)
                }
            }
        }
    }
}