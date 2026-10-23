import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jetbrains.kotlin.plugin.serialization")
}

val keystore = rootProject.file("keystore.properties").takeIf { it.exists() }?.let { f -> Properties().apply { f.inputStream().use(::load) } }

android {
    namespace = "com.orion.store"
    compileSdk = 37
    defaultConfig {
        applicationId = "com.orion.store"
        minSdk = 24
        targetSdk = 36
        versionCode = 29
        versionName = "2.2.5"
        // Phones and TVs are ARM; the x86 builds of the native libraries only served emulators
        ndk { abiFilters += listOf("arm64-v8a", "armeabi-v7a") }
    }
    splits {
        abi {
            isEnable = true
            reset()
            include("arm64-v8a", "armeabi-v7a")
            isUniversalApk = true
        }
    }
    signingConfigs {
        if (keystore != null) create("release") {
            storeFile = rootProject.file(keystore.getProperty("storeFile"))
            storePassword = System.getenv("ORION_STORE_PASSWORD") ?: keystore.getProperty("storePassword")
            keyAlias = keystore.getProperty("keyAlias")
            keyPassword = System.getenv("ORION_KEY_PASSWORD") ?: System.getenv("ORION_STORE_PASSWORD") ?: keystore.getProperty("keyPassword")
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.findByName("release") ?: signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    buildFeatures { compose = true; buildConfig = true; aidl = false }
    packaging {
        // Orion ships as a GitHub download, so the download size wins over install-time extraction of the .so files
        jniLibs.useLegacyPackaging = true
        // BouncyCastle data for algorithms Orion never touches (post-quantum Picnic tables, PKIX reviewer messages)
        resources.excludes += listOf("org/bouncycastle/pqc/**", "org/bouncycastle/x509/CertPathReviewerMessages*", "org/bouncycastle/pkix/CertPathReviewerMessages*")
    }
    testOptions.unitTests.isIncludeAndroidResources = true
}

dependencies {
    implementation(platform("androidx.compose:compose-bom:2026.09.00"))
    implementation("androidx.compose.material3:material3:1.5.0-beta01")
    implementation("androidx.compose.material3:material3-adaptive-navigation-suite:1.5.0-beta01")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")
    implementation("androidx.activity:activity-compose:1.13.0")
    implementation("androidx.core:core-ktx:1.19.1")
    implementation("androidx.core:core-splashscreen:1.2.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.11.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.11.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.11.0")
    implementation("io.coil-kt.coil3:coil-compose:3.6.3")
    implementation("io.coil-kt.coil3:coil-network-okhttp:3.6.3")
    implementation("com.squareup.okhttp3:okhttp:5.5.0")
    implementation("dev.rikka.shizuku:api:13.1.5")
    implementation("dev.rikka.shizuku:provider:13.1.5")
    // Built-in wireless ADB: installs as the shell user, which skips the unverified-developer 24-hour wait
    implementation("com.github.MuntashirAkon:libadb-android:3.1.1")
    implementation("org.bouncycastle:bcpkix-jdk15to18:1.81")
    implementation("org.conscrypt:conscrypt-android:2.7.0")
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.robolectric:robolectric:4.17")
    testImplementation("androidx.test:core:1.6.1")
    testImplementation("androidx.test.ext:junit:1.2.1")
}

// conscrypt-android's JNI only exists on devices; under Robolectric it shadows the JVM Conscrypt Robolectric ships and every test fails to boot
configurations.configureEach { if (name.endsWith("UnitTestRuntimeClasspath")) exclude(group = "org.conscrypt", module = "conscrypt-android") }

kotlin {
    compilerOptions {
        optIn.addAll(
            "androidx.compose.material3.ExperimentalMaterial3Api",
            "androidx.compose.material3.ExperimentalMaterial3ExpressiveApi",
            "androidx.compose.foundation.layout.ExperimentalLayoutApi",
            "androidx.compose.foundation.ExperimentalFoundationApi",
            "androidx.compose.animation.ExperimentalSharedTransitionApi",
            "kotlinx.coroutines.ExperimentalCoroutinesApi",
        )
    }
}

tasks.register("copyApksToOutput") {
    mustRunAfter(tasks.matching { it.name.startsWith("package") || it.name.startsWith("assemble") })
    val apkDir = layout.buildDirectory.dir("outputs/apk")
    val outputDir = rootProject.layout.projectDirectory.dir("output")
    val outputDirTypo = rootProject.layout.projectDirectory.dir("outupt")

    inputs.dir(apkDir)
    outputs.dir(outputDir)
    outputs.dir(outputDirTypo)

    doLast {
        val outDir = outputDir.asFile.apply { mkdirs(); listFiles()?.forEach { it.delete() } }
        val outDirTypo = outputDirTypo.asFile.apply { mkdirs(); listFiles()?.forEach { it.delete() } }
        apkDir.get().asFileTree.matching {
            include("**/*.apk")
        }.forEach { apkFile ->
            val targetName = when {
                apkFile.name.contains("arm64-v8a") -> "orion-zero-arm64-v8a.apk"
                apkFile.name.contains("armeabi-v7a") -> "orion-zero-armeabi-v7a.apk"
                apkFile.name.contains("universal") -> "orion-zero-universal.apk"
                else -> null
            }
            if (targetName != null) {
                apkFile.copyTo(File(outDir, targetName), overwrite = true)
                apkFile.copyTo(File(outDirTypo, targetName), overwrite = true)
            }
        }
    }
}
