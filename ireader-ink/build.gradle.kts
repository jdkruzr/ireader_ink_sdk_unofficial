plugins {
    id("com.android.library")
}

android {
    namespace = "io.github.ireaderunofficial.ink"
    compileSdk = 36
    buildToolsVersion = "36.0.0"

    defaultConfig {
        minSdk = 26
        consumerProguardFiles("consumer-rules.pro")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    // The device provides android.eink.*; never package the stubs.
    compileOnly(project(":ireader-ink-stubs"))
}
