buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath(libs.plugin.androidGradlePlugin)
        classpath(libs.plugin.kotlin)
    }
}

allprojects {
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"

        kotlinOptions.allWarningsAsErrors = false

        kotlinOptions.freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
        kotlinOptions.freeCompilerArgs += "-opt-in=kotlin.Experimental"
    }
}
