// Top-level build file for Platinum Arabic AI Assistant

plugins {
    id("com.android.application") version "8.2.0" apply false
    id("com.android.library") version "8.2.0" apply false
    kotlin("android") version "1.9.22" apply false
    kotlin("jvm") version "1.9.22" apply false
    id("com.google.dagger.hilt.android") version "2.49" apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

// Global configuration
allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
