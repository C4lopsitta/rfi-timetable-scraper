import org.gradle.internal.declarativedsl.dom.resolution.resolutionContainer
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    kotlin("plugin.serialization") version "2.0.0"
}

kotlin {
    jvm("desktop")

    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    val xcf = XCFramework()

    iosArm64(){
        binaries.framework (

        ) {
            baseName = "ComposeApp"
            isStatic = true
            embedBitcode("bitcode")
            xcf.add(this)
        }
    }

    iosSimulatorArm64 {
        binaries.framework (

        ) {
            baseName = "ComposeApp"
            isStatic = true
            embedBitcode("bitcode")
            xcf.add(this)
        }
    }

    sourceSets {
        val desktopMain by getting

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(compose.ui) {
                exclude (group = "androidx.compose.ui", module = "ui-unit-desktop")
            }
        }

        commonMain {
            kotlin.srcDir("build/generated-src/kotlin")
        }

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.ktor.client.core)
            implementation(libs.material3)
            implementation(libs.material.icons.extended)
            implementation(libs.ksoup)
            implementation(libs.ksoup.network)
            implementation(libs.navigation.compose)
            implementation(libs.lifecycle.viewmodel.compose)
            implementation(libs.ktor.client.cio)
            implementation(libs.datastore)
            implementation(libs.datastore.preferences)
            implementation(libs.kotlinx.serialization.json.jvm)
            implementation(libs.kotlinx.datetime)
        }
        desktopMain.dependencies {
            implementation(compose.material3)
            implementation(compose.desktop.currentOs) {
                exclude("org.jetbrains.compose.material")
                exclude("io.ktor.client.engine.okhttp")
            }
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.kotlinx.serialization.core)
        }
    }
}

android {
    namespace = "cc.atomtech.timetable"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "cc.atomtech.timetable"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 16
        versionName = "1.4.3"
        versionNameSuffix = "-play"
        resourceConfigurations.add("en")
        resourceConfigurations.add("it")
        multiDexEnabled = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1,MD,LICENSE.md,NOTICE.md}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildToolsVersion = "34.0.0"
    flavorDimensions += listOf("playless", "playful")
}

dependencies {
    implementation(libs.androidx.databinding.compiler.common)
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "cc.atomtech.timetable"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb, TargetFormat.Pkg, TargetFormat.Rpm, TargetFormat.Exe)
            packageName = "Timetable"
            packageVersion = "1.3.8"
            description = "Scraper app that uses RFI's Arrivi&Partenze website to visualize departures and arrivals for any RFI-Managed railway station"
        }

        buildTypes.release.proguard {
            version.set("7.5.0")
            configurationFiles.from("proguard.pro")
        }
    }
}


val buildConfigGenerator by tasks.registering(Sync::class) {
    from(
        resources.text.fromString(
            """
        |package my.project
        |
        |object BuildConfig {
        |  const val PROJECT_VERSION = "${version ?: "error"}"
        |}
        |
      """.trimMargin()
        )
    ) {
        rename { "BuildConfig.kt" } // set the file name
        into("my/project/") // change the directory to match the package
    }

    into(layout.buildDirectory.dir("generated-src/kotlin/"))
}
