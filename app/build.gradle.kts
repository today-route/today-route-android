plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("com.google.gms.google-services")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = AppConfig.compileSdk

    defaultConfig {
        applicationId = "com.maru.todayroute"
        minSdk = AppConfig.minSdk
        targetSdk = AppConfig.targetSdk
        versionCode = AppConfig.versionCode
        versionName = AppConfig.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    kapt {
        correctErrorTypes = true
    }
}

dependencies {
    // AndroidX
    implementation(Dep.AndroidX.CORE)
    implementation(Dep.AndroidX.APP_COMPAT)
    implementation(Dep.AndroidX.MATERIAL)
    implementation(Dep.AndroidX.CONSTRAINT_LAYOUT)
    implementation(Dep.AndroidX.LEGACY_SUPPORT)
    implementation(Dep.AndroidX.LOCATION)
    implementation(platform(Dep.AndroidX.BOM))
    implementation(Dep.AndroidX.DYNAMIC_LINK)
    implementation(Dep.AndroidX.ANALYTICS)
    implementation(Dep.AndroidX.ACTIVITY)
    implementation(Dep.AndroidX.FRAGMENT)

    // Lifecycle
    implementation(Dep.AndroidX.LIFECYCLE.RUNTIME)
    implementation(Dep.AndroidX.LIFECYCLE.VIEW_MODEL)
    implementation(Dep.AndroidX.LIFECYCLE.LIVE_DATA)

    // Navigation
    implementation(Dep.AndroidX.Navigation.NAVIGATION_FRAGMENT)
    implementation(Dep.AndroidX.Navigation.NAVIGATION_UI)

    // Hilt
    implementation(Dep.AndroidX.Hilt.HILT_ANDROID)
    kapt(Dep.AndroidX.Hilt.HILT_ANDROID_COMPILER)

    // Kotlin
    implementation(Dep.Kotlin.COROUTINE)

    // Test
    testImplementation(Dep.Test.JUNIT)
    androidTestImplementation(Dep.Test.EXT_JUNIT)
    androidTestImplementation(Dep.Test.ESPRESSO)

    // Libraries
    implementation(Dep.Libraries.NAVER_MAPS)
    implementation(Dep.Libraries.KAKAO_LOGIN)
    implementation(Dep.Libraries.KAKAO_SHARE)

    // Glide
    implementation(Dep.Libraries.Glide.GLIDE)
    annotationProcessor(Dep.Libraries.Glide.GLIDE_COMPILER)

    // Module
    implementation(project(":data"))
}