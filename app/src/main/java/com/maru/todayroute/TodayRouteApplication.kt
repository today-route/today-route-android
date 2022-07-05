package com.maru.todayroute

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.maru.todayroute.util.Constants
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TodayRouteApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, Constants.KAKAO_NATIVE_APP_KEY)
    }
}