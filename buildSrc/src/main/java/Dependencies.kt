object Versions {

    // AndroidX
    const val CORE = "1.8.0"
    const val APP_COMPAT = "1.4.2"
    const val MATERIAL = "1.6.1"
    const val CONSTRAINT_LAYOUT = "2.1.4"
    const val LEGACY_SUPPORT = "1.0.0"
    const val LOCATION = "20.0.0"
    const val SECRET_GRADLE = "2.0.1"
    const val GMS = "4.3.13"
    const val BOM = "30.3.2"
    const val DATA_STORE = "1.0.0"
    const val ACTIVITY = "1.5.1"
    const val FRAGMENT = "1.5.2"

    // Kotlin
    const val COROUTINE = "1.3.9"

    // Test
    const val JUNIT = "4.13.2"
    const val EXT_JUNIT = "1.1.3"
    const val ESPRESSO = "3.4.0"

    // Libraries
    const val NAVER_MAPS = "3.14.0"
    const val KAKAO = "2.11.0"
    const val JWT_DECODE = "2.0.1"
}

object Dep {

    object AndroidX {
        const val CORE = "androidx.core:core-ktx:${Versions.CORE}"
        const val APP_COMPAT = "androidx.appcompat:appcompat:${Versions.APP_COMPAT}"
        const val MATERIAL = "com.google.android.material:material:${Versions.MATERIAL}"
        const val CONSTRAINT_LAYOUT = "androidx.constraintlayout:constraintlayout:${Versions.CONSTRAINT_LAYOUT}"
        const val LEGACY_SUPPORT = "androidx.legacy:legacy-support-v4:${Versions.LEGACY_SUPPORT}"
        const val LOCATION = "com.google.android.gms:play-services-location:${Versions.LOCATION}"
        const val GMS = "com.google.gms:google-services:${Versions.GMS}"
        const val SECRET_GRADLE_PLUGIN = "com.google.android.libraries.mapsplatform.secrets-gradle-plugin:secrets-gradle-plugin:${Versions.SECRET_GRADLE}"
        const val DATA_STORE = "androidx.datastore:datastore-preferences:${Versions.DATA_STORE}"
        const val ACTIVITY = "androidx.activity:activity-ktx:${Versions.ACTIVITY}"
        const val FRAGMENT = "androidx.fragment:fragment-ktx:${Versions.FRAGMENT}"

        object LIFECYCLE {
            private const val VERSION = "2.4.1"
            const val RUNTIME = "androidx.lifecycle:lifecycle-runtime-ktx:${VERSION}"
            const val VIEW_MODEL = "androidx.lifecycle:lifecycle-viewmodel-ktx:${VERSION}"
            const val LIVE_DATA = "androidx.lifecycle:lifecycle-livedata-ktx:${VERSION}"
        }

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

    object Firebase {
        const val BOM = "com.google.firebase:firebase-bom:${Versions.BOM}"
        const val DYNAMIC_LINK = "com.google.firebase:firebase-dynamic-links-ktx"
        const val ANALYTICS = "com.google.firebase:firebase-analytics-ktx"
        const val FIRESTORE = "com.google.firebase:firebase-firestore-ktx"
    }

    object Kotlin {
        const val COROUTINE = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.COROUTINE}"
    }

    object Test {
        const val JUNIT = "junit:junit:${Versions.JUNIT}"
        const val EXT_JUNIT = "androidx.test.ext:junit:${Versions.EXT_JUNIT}"
        const val ESPRESSO = "androidx.test.espresso:espresso-core:${Versions.ESPRESSO}"
    }

    object Libraries {
        const val NAVER_MAPS = "com.naver.maps:map-sdk:${Versions.NAVER_MAPS}"
        const val KAKAO_LOGIN = "com.kakao.sdk:v2-user:${Versions.KAKAO}"
        const val KAKAO_SHARE = "com.kakao.sdk:v2-share:${Versions.KAKAO}"
        const val JWT_DECODE = "com.auth0.android:jwtdecode:${Versions.JWT_DECODE}"

        object Glide {
            private const val VERSION = "4.13.2"
            const val GLIDE = "com.github.bumptech.glide:glide:${VERSION}"
            const val GLIDE_COMPILER = "com.github.bumptech.glide:compiler:${VERSION}"
        }

        object Retrofit {
            private const val VERSION = "2.9.0"
            const val RETROFIT = "com.squareup.retrofit2:retrofit:${VERSION}"
            const val RETROFIT_CONVERTER = "com.squareup.retrofit2:converter-moshi:${VERSION}"
        }

        object Moshi {
            private const val VERSION = "1.13.0"
            const val MOSHI = "com.squareup.moshi:moshi:${VERSION}"
            const val MOSHI_KOTLIN_CODEGEN = "com.squareup.moshi:moshi-kotlin-codegen:${VERSION}"
        }

        object OkHttp3 {
            private const val VERSION = "4.9.3"
            const val OK_HTTP = "com.squareup.okhttp3:okhttp:${VERSION}"
            const val LOGGING_INTERCEPTOR = "com.squareup.okhttp3:logging-interceptor:${VERSION}"
        }
    }
}

