plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
    id("io.gitlab.arturbosch.detekt") version "1.23.0"

}

android {
    namespace = "com.example.xbcad7311"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.xbcad7311"
        minSdk = 34
        targetSdk = 34
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
    viewBinding {
        enable = true
    }
    dataBinding {
        enable = true
    }
    packagingOptions {
        excludes += "META-INF/INDEX.LIST"
        excludes += "META-INF/*.SF"
        excludes += "META-INF/*.DSA"
        excludes += "META-INF/io.netty.versions.properties"
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.firebase.firestore)
    implementation(libs.car.ui.lib)
    implementation(libs.firebase.database.ktx)
    implementation(libs.play.services.wallet)
    implementation(libs.transportation.consumer)
    implementation(libs.firebase.functions.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation ("com.google.firebase:firebase-auth-ktx:23.1.0")
    implementation ("androidx.biometric:biometric:1.1.0")
    implementation("io.ktor:ktor-server-netty:2.2.4")
    implementation("io.ktor:ktor-server-core:2.2.4")
    implementation("io.ktor:ktor-server-host-common:2.2.4")
    implementation("io.ktor:ktor-server-content-negotiation:2.2.4")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.2.4")
    implementation("io.ktor:ktor-server-status-pages:2.2.4")
    implementation("io.ktor:ktor-server-request-validation:2.2.4")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
    implementation ("com.stripe:stripe-android:21.0.0")
    implementation ("com.google.firebase:firebase-functions-ktx:20.0.0")
    implementation ("com.google.android.gms:play-services-wallet:19.4.0")
    implementation ("com.paypal.sdk:paypal-android-sdk:2.16.0")
    val billing_version = "7.1.1"

    implementation("com.android.billingclient:billing-ktx:7.1.1")
}