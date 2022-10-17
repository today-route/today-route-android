package com.maru.todayroute.ui.initial

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
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
import com.maru.data.repository.InitialRepository
import com.maru.todayroute.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

@HiltViewModel
class InitialViewModel @Inject constructor(
    private val initialRepository: InitialRepository
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
    val inviteCode get() = _inviteCode
    private var _inviteCode: String = ""

    private val _moveToConnectCoupleFragment = SingleLiveEvent<Unit>()
    val moveToConnectCoupleFragment: LiveData<Unit> get() = _moveToConnectCoupleFragment
    private val _moveToInitialUserInfoFragment = SingleLiveEvent<Unit>()
    val moveToInitialUserInfoFragment: LiveData<Unit> get() = _moveToInitialUserInfoFragment
    private val _moveToMainActivity = SingleLiveEvent<Unit>()
    val moveToMainActivity: LiveData<Unit> get() = _moveToMainActivity

    private val calendar: Calendar = GregorianCalendar()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val date = calendar.get(Calendar.DATE)

    private lateinit var coupleUser: User
    private val _showCoupleNameCheckDialog = SingleLiveEvent<String>()
    val showCoupleNameCheckDialog: LiveData<String> get() = _showCoupleNameCheckDialog

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
//            getSignInUserCoupleInfoByAccessToken()
        } else {
            _moveToInitialUserInfoFragment.call()
        }
    }

//    private fun getSignInUserCoupleInfoByAccessToken() {
//        // TODO: access token으로 요청했을 때 user, couple 정보 받아오기
//    }

    fun setUserGender(gender: Gender) {
        this.gender = gender
    }

    fun setUserBirthday(birthday: String) {
        this.birthday = birthday
    }

    fun setInviteCode(inviteCode: String) {
        this._inviteCode = inviteCode
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
            val id = result.getOrNull()!!.user.id
            _code = result.getOrNull()!!.user.code
            this.id = id
            initialRepository.saveSignInUserId(id)
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

    suspend fun connectCoupleByCode(code: String) {
        findUserByInviteCode(code)
    }

    private suspend fun findUserByInviteCode(code: String) {
        val result = withContext(viewModelScope.coroutineContext) {
            initialRepository.findUserByInviteCode(code)
        }

        if (result.isSuccess) {
            val user = result.getOrNull()
            if (user == null || user.id == -1) {
                // TODO: 실패
            } else {
                coupleUser = user
                _showCoupleNameCheckDialog.value = user.nickname
            }
        }
    }

    suspend fun registerNewCouple() {
        val result = withContext(viewModelScope.coroutineContext) {
            initialRepository.registerNewCouple(
                CoupleInfo(
                    startDate = startDate,
                    user1Id = id,
                    user2Id = coupleUser.id
                )
            )
        }

        if (result.isSuccess) {
            val coupleInfo = result.getOrNull()!!
            startTodayRoute(coupleInfo.id)
        }
    }

    suspend fun tryStart() {
        // 1. user2ID랑 여기 id랑 같은 게 있는지 확인
        val result = withContext(viewModelScope.coroutineContext) {
            initialRepository.findCoupleInfoById(id)
        }

        if (result.isSuccess) {
            val coupleInfo = result.getOrNull()
            if (coupleInfo == null || coupleInfo.id == -1) {
                // TODO: 연결 실패했다고 메세지 띄우기
            } else {
                startTodayRoute(coupleInfo.id)
            }
        }
    }

    private suspend fun startTodayRoute(coupleId: Int) {
        initialRepository.saveCoupleId(coupleId)
        _moveToMainActivity.call()
    }
}