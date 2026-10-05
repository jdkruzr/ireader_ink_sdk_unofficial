// Compile-time signatures for the iReader/Zhangyue firmware ink API (android.eink.*), which ships in
// the device's boot classpath (framework.jar), e.g. on the Musnap Ocean. Depend on this module with
// `compileOnly` ONLY: it must never be packaged, so the device's real classes are the ones that load.
// Bodies throw like android.jar stubs. Add members only as they are confirmed on firmware.
plugins {
    id("com.android.library")
}

android {
    namespace = "io.github.ireaderunofficial.ink.stubs"
    compileSdk = 36
    buildToolsVersion = "36.0.0"

    defaultConfig {
        minSdk = 26
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
