plugins {
    id("com.android.library")
    kotlin("android")
    id("lib-publish-android")
}

ext["libraryName"] = "flow-call-adapter-factory"
ext["libraryVersion"] = "1.0.1"
ext["description"] = "Android Retrofit FlowCallAdapterFactory"
ext["url"] = "https://thdev.tech/Retrofit-FlowCallAdapterFactory/"

android {
    namespace = "tech.thdev.network.flowcalladapterfactory"
    buildToolsVersion = libs.versions.buildToolsVersion.get()
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    // AGP 8.0
    publishing {
        multipleVariants("release") {
            allVariants()
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    // Junit 5 사용을 위해
    testOptions {
        unitTests.all {
            it.useJUnitPlatform()
        }
    }
}

dependencies {
    api(libs.kotlin.stdlib)
    api(libs.network.retrofit)
    api(libs.coroutines.core)

    testImplementation(libs.test.coroutines)
    testImplementation(libs.test.coroutines.turbine)
    testImplementation(libs.test.junit5)
    testImplementation(libs.test.mockito)
    testImplementation(libs.test.mockito.kotlin)
    testRuntimeOnly(libs.test.junit5.engine)
    testImplementation(libs.test.okhttp.mock)
}