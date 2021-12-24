import tech.thdev.gradle.dependencies.Dependency
import tech.thdev.gradle.locals.apis
import tech.thdev.gradle.locals.testImplementations
import tech.thdev.gradle.locals.testRuntimeOnlys

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("lib-publish")
}

ext["libraryName"] = "flow-call-adapter-factory"
ext["libraryVersion"] = "1.0.0"
ext["description"] = "Android Retrofit FlowCallAdapterFactory"
ext["url"] = "https://thdev.tech/Retrofit-FlowCallAdapterFactory/"

android {
    compileSdk = Dependency.Base.compileVersion
    buildToolsVersion = Dependency.Base.buildToolsVersion

    defaultConfig {
        minSdk = Dependency.Base.minSdk
        targetSdk = Dependency.Base.targetSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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

    kotlinOptions {
        jvmTarget = "1.8"
    }

    // buildconfig 생성하지 않기
    libraryVariants.all {
        generateBuildConfigProvider.configure {
            enabled = false
        }
    }
    // Junit 5 사용을 위해
    testOptions {
        unitTests.all {
            it.useJUnitPlatform()
        }
    }
}

dependencies {
    apis(
        Dependency.Kotlin.stdLib,
        Dependency.Network.retrofit,
        Dependency.Coroutines.core,
    )

    Dependency.Coroutines.run {
        testImplementations(
            turbine,
            test
        )
    }
    Dependency.AndroidTest.run {
        testImplementations(
            junit5,
            mockito,
            mockitoKotlin,
        )
        testRuntimeOnlys(
            engine,
        )
    }
    testImplementation(Dependency.Network.okhttp3MockWebServer)
}