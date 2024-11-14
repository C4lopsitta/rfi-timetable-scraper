import org.gradle.internal.declarativedsl.dom.resolution.resolutionContainer
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }
    
    jvm("desktop")
    
    sourceSets {
        val desktopMain by getting

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
//            implementation(libs.ktor.client.okhttp)
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
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
        }
    }
}

android {
    namespace = "cc.atomtech.timetable"
    compileSdkVersion(libs.versions.android.compileSdk.get().toInt())

    defaultConfig {
        applicationId = "cc.atomtech.timetable"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 3
        versionName = "1.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
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
}

dependencies {
    implementation(libs.androidx.databinding.compiler.common)
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "cc.atomtech.timetable.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb, TargetFormat.AppImage, TargetFormat.Pkg, TargetFormat.Rpm, TargetFormat.Exe)
            packageName = "Timetable"
            packageVersion = "1.3.0"
            description = "Scraper app that uses RFI's Arrivi&Partenze website to visualize departures and arrivals for any RFI-Managed railway station"
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