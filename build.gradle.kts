buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath(tech.thdev.gradle.dependencies.Gradle.android)
        classpath(tech.thdev.gradle.dependencies.Gradle.kotlin)
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}