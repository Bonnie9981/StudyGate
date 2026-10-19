plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "tw.studygate"
    compileSdk = 35

    defaultConfig {
        applicationId = "tw.studygate"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "0.1.0"
    }

    buildFeatures { buildConfig = true }
}
