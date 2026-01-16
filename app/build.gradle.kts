plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    id("org.jetbrains.kotlin.plugin.serialization")

    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    id("androidx.room") version "2.8.4"
}


android {
    namespace = "com.example.driverlogisticsapp"
    compileSdk {
        version = release(36)
    }

    room {
        schemaDirectory("$projectDir/schemas")
    }

    defaultConfig {
        applicationId = "com.example.driverlogisticsapp"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures{
        compose = true
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.androidx.hilt.common)
    implementation(libs.androidx.hilt.work)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


    // COMPOSE
    val composeBom = platform("androidx.compose:compose-bom:2025.12.00")
    val navVersion = "2.9.6"
    implementation(composeBom)
    androidTestImplementation(composeBom)

    // Material Design 3
    implementation("androidx.compose.material3:material3")

    // Android Studio Preview support
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")

    // UI Tests
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    // Testing Navigation
    androidTestImplementation("androidx.navigation:navigation-testing:$navVersion")

    // Integration with activities
    implementation("androidx.activity:activity-compose:1.11.0")

    // Compose Navigation
    implementation("androidx.navigation:navigation-compose:${navVersion}")

    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.x")

    implementation("androidx.compose.runtime:runtime-livedata")


    // HILT
    implementation("com.google.dagger:hilt-android:2.57.1")
    ksp("com.google.dagger:hilt-android-compiler:2.57.1")

    ksp("androidx.hilt:hilt-compiler:1.2.0")


    // ROOM
    val roomVersion = "2.8.4"
    implementation("androidx.room:room-runtime:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")

    // Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:${roomVersion}")


    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    // Retrofit with Scalar Converter
    implementation("com.squareup.retrofit2:converter-scalars:2.11.0")
    // Gson
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")


    // WORK MANAGER
    val workVersion = "2.11.0"

    // Kotlin + coroutines
    implementation(libs.androidx.work.runtime.ktx)
}