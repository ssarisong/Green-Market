import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.dokka.base.DokkaBaseConfiguration
import java.net.URL

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("org.jetbrains.dokka")
}

android {
    namespace = "kr.ac.hansung.greenmarket"
    compileSdk = 34

    defaultConfig {
        applicationId = "kr.ac.hansung.greenmarket"
        minSdk = 33
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

buildscript {
    dependencies {
        classpath("org.jetbrains.dokka:dokka-base:1.9.10")
    }
}

tasks.withType<DokkaTask>().configureEach {
    outputDirectory.set(file("../docs/html"))
    dokkaSourceSets {
        register("FrontendSet"){
            this.displayName.set("Frontend")
            this.sourceRoots.from(file("src/main/java/kr/ac/hansung/greenmarket/utils"))
            this.sourceRoots.from(file("src/main/java/kr/ac/hansung/greenmarket/ui"))
        }
        register("BackendSet"){
            this.displayName.set("Backend")
            this.sourceRoots.from(file("src/main/java/kr/ac/hansung/greenmarket/utils"))
            this.sourceRoots.from(file("src/main/java/kr/ac/hansung/greenmarket/models"))
        }
    }
    pluginConfiguration<DokkaBase, DokkaBaseConfiguration> {
        footerMessage = "(c) 2023 Hansung University"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation(platform("com.google.firebase:firebase-bom:32.4.0"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")
}