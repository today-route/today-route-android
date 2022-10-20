package com.maru.todayroute.ui.initial

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.template.model.Link
import com.kakao.sdk.template.model.TextTemplate
import com.kakao.sdk.user.UserApiClient
import com.maru.data.model.CoupleInfo
import com.maru.data.model.Gender
import com.maru.data.model.User
import com.maru.data.network.SignUpRequest
import com.maru.data.network.Token
import com.maru.data.repository.InitialRepository
import com.maru.data.repository.TokenRepository
import com.maru.todayroute.SignInTokenInfo
import com.maru.todayroute.SignInTokenInfo.token
import com.maru.todayroute.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

@HiltViewModel
class InitialViewModel @Inject constructor(
    private val initialRepository: InitialRepository,
    private val tokenRepository: TokenRepository
) : ViewModel() {

    private var id = -1
    private lateinit var key: String
    private lateinit var gender: Gender
    private lateinit var email: String
    private lateinit var profileUrl: String
    private lateinit var nickname: String
    private lateinit var birthday: String

    val startDate get() = _startDate
    private var _startDate = ""
    val code get() = _code
    private lateinit var _code: String
    val inviteCode: LiveData<String> get() = _inviteCode
    private var _inviteCode: MutableLiveData<String> = MutableLiveData("")

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
            initialRepository.signInUser(key)
        }

        if (result.isSuccess) {
            token = result.getOrNull()!!
            viewModelScope.launch(Dispatchers.IO) {
                tokenRepository.saveTokens(token)
            }
            getSignInCoupleInfoByAccessToken()
        } else {
            _moveToInitialUserInfoFragment.call()
        }
    }

    private suspend fun getSignInCoupleInfoByAccessToken() {
        val result = withContext(viewModelScope.coroutineContext) {
            initialRepository.getMyCoupleData()
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
            initialRepository.getMyUserData()
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

    fun setUserBirthday(birthday: String) {
        this.birthday = birthday
    }

    fun setInviteCode(inviteCode: String) {
        this._inviteCode.value = inviteCode
    }

    fun setStartDate(startDate: String) {
        this._startDate = startDate
    }

    suspend fun registerNewUser() {
        val result = withContext(viewModelScope.coroutineContext) {
            initialRepository.registerNewUser(
                SignUpRequest(
                    key,
                    gender,
                    email,
                    nickname,
                    profileUrl,
                    birthday
                )
            )
        }

        if (result.isSuccess) {
            val accessToken = result.getOrNull()!!.access
            val refreshToken = result.getOrNull()!!.refresh
            token = Token(accessToken, refreshToken)
            tokenRepository.saveTokens(token)
            setSignInUser()
            _moveToConnectCoupleFragment.call()
        }
    }

    fun makeInviteTextTemplate(inviteLink: Uri): TextTemplate =
        TextTemplate(
            text = """
                    ${nickname}님이 함께 오늘의 길을 만들고 싶어해요!
                    초대 코드 : $code
                """.trimIndent(),
            link = Link(mobileWebUrl = inviteLink.toString()),
            buttonTitle = "초대 코드 입력하기"
        )

    suspend fun tryStart() {
        val result = withContext(viewModelScope.coroutineContext) {
            initialRepository.getMyCoupleData()
        }

        if (result.isSuccess) {
            _moveToMainActivity.call()
        } else {
            // TODO: 다시 시도 알림
        }
    }

    suspend fun connectCoupleByCode() {
        val result = withContext(viewModelScope.coroutineContext) {
            val inviteCode = _inviteCode.value ?: ""
            initialRepository.registerNewCouple(inviteCode, startDate)
        }

        if (result.isSuccess) {
            _moveToMainActivity.call()
        } else {
            // TODO: 다시 시도 알림
        }
    }
}