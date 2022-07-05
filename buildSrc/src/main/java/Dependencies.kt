object Versions {

    // AndroidX
    const val CORE = "1.8.0"
    const val APP_COMPAT = "1.4.2"
    const val MATERIAL = "1.6.1"
    const val CONSTRAINT_LAYOUT = "2.1.4"
    const val LEGACY_SUPPORT = "1.0.0"
    const val LIFECYCLE = "2.4.1"
    const val LOCATION = "20.0.0"

    // Test
    const val JUNIT = "4.13.2"
    const val EXT_JUNIT = "1.1.3"
    const val ESPRESSO = "3.4.0"

    // Libraries
    const val NAVER_MAPS = "3.14.0"
    const val KAKAO_LOGIN = "2.11.0"
}

object Dep {

    object AndroidX {
        const val CORE = "androidx.core:core-ktx:${Versions.CORE}"
        const val APP_COMPAT = "androidx.appcompat:appcompat:${Versions.APP_COMPAT}"
        const val MATERIAL = "com.google.android.material:material:${Versions.MATERIAL}"
        const val CONSTRAINT_LAYOUT = "androidx.constraintlayout:constraintlayout:${Versions.CONSTRAINT_LAYOUT}"
        const val LEGACY_SUPPORT = "androidx.legacy:legacy-support-v4:${Versions.LEGACY_SUPPORT}"
        const val LIFECYCLE = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.LIFECYCLE}"
        const val LOCATION = "com.google.android.gms:play-services-location:${Versions.LOCATION}"

        object Navigation {
            private const val VERSION = "2.4.2"
            const val NAVIGATION_FRAGMENT = "androidx.navigation:navigation-fragment-ktx:${VERSION}"
            const val NAVIGATION_UI = "androidx.navigation:navigation-ui-ktx:${VERSION}"
            const val NAVIGATION_SAFE_ARGS_PLUGIN = "androidx.navigation:navigation-safe-args-gradle-plugin:${VERSION}"
        }

        object Hilt {
            private const val VERSION = "2.41"
            const val HILT_PLUGIN = "com.google.dagger:hilt-android-gradle-plugin:${VERSION}"
            const val HILT_ANDROID = "com.google.dagger:hilt-android:${VERSION}"
            const val HILT_ANDROID_COMPILER = "com.google.dagger:hilt-android-compiler:${VERSION}"
        }
    }

    object Test {
        const val JUNIT = "junit:junit:${Versions.JUNIT}"
        const val EXT_JUNIT = "androidx.test.ext:junit:${Versions.EXT_JUNIT}"
        const val ESPRESSO = "androidx.test.espresso:espresso-core:${Versions.ESPRESSO}"
    }

    object Libraries {
        const val NAVER_MAPS = "com.naver.maps:map-sdk:${Versions.NAVER_MAPS}"
        const val KAKAO_LOGIN = "com.kakao.sdk:v2-user:${Versions.KAKAO_LOGIN}"
    }
}

