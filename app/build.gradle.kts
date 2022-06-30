plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("androidx.navigation.safeargs.kotlin")
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
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    // AndroidX
    implementation(Dep.AndroidX.CORE)
    implementation(Dep.AndroidX.APP_COMPAT)
    implementation(Dep.AndroidX.MATERIAL)
    implementation(Dep.AndroidX.CONSTRAINT_LAYOUT)
    implementation(Dep.AndroidX.LEGACY_SUPPORT)
    implementation(Dep.AndroidX.LIFECYCLE)
    implementation(Dep.AndroidX.LOCATION)

    // Navigation
    implementation(Dep.AndroidX.Navigation.NAVIGATION_FRAGMENT)
    implementation(Dep.AndroidX.Navigation.NAVIGATION_UI)

    // Test
    testImplementation(Dep.Test.JUNIT)
    androidTestImplementation(Dep.Test.EXT_JUNIT)
    androidTestImplementation(Dep.Test.ESPRESSO)

    // Libraries
    implementation(Dep.Libraries.NAVER_MAPS)

    // Module
    implementation(project(":data"))
}