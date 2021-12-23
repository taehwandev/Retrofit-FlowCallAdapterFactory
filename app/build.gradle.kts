import tech.thdev.gradle.dependencies.Dependency

plugins {
    id("com.android.application")

    kotlin("android")
    kotlin("kapt")
}

android {
    compileSdk = Dependency.Base.compileVersion
    buildToolsVersion = Dependency.Base.buildToolsVersion

    defaultConfig {
        minSdk = Dependency.Base.minSdk
        targetSdk = Dependency.Base.targetSdk
        vectorDrawables.useSupportLibrary = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }

        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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

    testOptions {
        unitTests.isReturnDefaultValues = true
    }
}

dependencies {
    implementation(Dependency.Kotlin.stdLib)

    implementation(Dependency.Google.material)

    implementation(Dependency.AndroidX.coreKtx)
    implementation(Dependency.AndroidX.appCompat)
    implementation(Dependency.AndroidX.activity)
    implementation(Dependency.AndroidX.constraintLayout)
    implementation(Dependency.AndroidX.vectorDrawable)

    implementation(Dependency.Network.retrofit)
    implementation(Dependency.Network.okhttp)
    implementation(Dependency.Network.okhttpLogging)

    Dependency.AndroidTest.run {
        testImplementation(junit5)
        testImplementation(mockito)
        testImplementation(mockitoKotlin)
        testRuntimeOnly(engine)
    }
}