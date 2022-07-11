plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = 32

    defaultConfig {
        minSdk = 23
        targetSdk = 32

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    kapt {
        correctErrorTypes = true
    }
}

dependencies {
    implementation(Dep.AndroidX.CORE)
    testImplementation(Dep.Test.JUNIT)
    androidTestImplementation(Dep.Test.EXT_JUNIT)
    androidTestImplementation(Dep.Test.ESPRESSO)

    // Hilt
    implementation(Dep.AndroidX.Hilt.HILT_ANDROID)
    kapt(Dep.AndroidX.Hilt.HILT_ANDROID_COMPILER)

    // Retrofit
    implementation(Dep.Libraries.Retrofit.RETROFIT)
    implementation(Dep.Libraries.Retrofit.RETROFIT_CONVERTER)

    // Moshi
    implementation(Dep.Libraries.Moshi.MOSHI)
    kapt(Dep.Libraries.Moshi.MOSHI_KOTLIN_CODEGEN)

    // OkHttp3
    implementation(Dep.Libraries.OkHttp3.OK_HTTP)
    implementation(Dep.Libraries.OkHttp3.LOGGING_INTERCEPTOR)
}