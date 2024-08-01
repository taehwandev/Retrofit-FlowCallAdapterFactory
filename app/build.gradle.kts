plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "tech.thdev.network.flowcalladapterfactory.sample"
    buildToolsVersion = libs.versions.buildToolsVersion.get()
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = "${libs.versions.major.get()}.${libs.versions.minor.get()}.${libs.versions.hotfix.get()}"

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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compilerVersion.get()
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    testOptions {
        unitTests.isReturnDefaultValues = true
    }

    packaging {
        resources {
            excludes += "/META-INF/*"
        }
    }
}

dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(libs.coroutines.android)

    implementation(libs.androidx.core)
    implementation(libs.google.material)

    implementation(libs.compose.activity)
    implementation(libs.compose.ui)
    implementation(libs.compose.runtime)
    implementation(libs.compose.foundation)
    implementation(libs.compose.material3)
    implementation(libs.compose.uiToolingPreview)

    implementation(libs.network.retrofit)
    implementation(libs.network.okhttp)
    implementation(libs.network.okhttp.logging)

    implementation(projects.flowCallAdapterFactory)

    implementation(libs.test.junit5)
    implementation(libs.test.mockito)
    implementation(libs.test.mockito.kotlin)
    testRuntimeOnly(libs.test.junit5.engine)
}
