// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    id("com.google.dagger.hilt.android") version "2.50" apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

buildscript {
    extra.apply {
        set("compose_version", "1.5.4")
        set("hilt_version", "2.50")
    }
    dependencies {
        classpath("com.google.dagger:hilt-android-gradle-plugin:${property("hilt_version")}")
    }
}
