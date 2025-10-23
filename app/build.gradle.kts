plugins {
    id("com.android.application")
    kotlin("android")
    // kotlin("kapt") removed (we use KSP instead)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.ksp)



}

android {
    namespace = "com.example.blisterapp"
    compileSdk = 36 // actualizado a 36

    defaultConfig {
        applicationId = "com.example.blisterapp"
        minSdk = 26 // tal como pediste
        targetSdk = 36
        versionCode = 1
        versionName = "0.1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
    }

    kotlin {
        jvmToolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
    }



    kotlinOptions {
        jvmTarget = "17"
    }

    kotlin {
        // Ensure Kotlin uses a Java 17 toolchain (works with Kotlin 1.8+ / 2.x)
        jvmToolchain(17)
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = "17"
        }
    }

    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Compose (usar BOM para consistencia si lo deseas)
    implementation(platform("androidx.compose:compose-bom:2024.04.00")) // actualizar según release
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    debugImplementation("androidx.compose.ui:ui-tooling")

    // Navigation Compose
    implementation("androidx.navigation:navigation-compose:2.6.0")

    // Lifecycle / ViewModel - Compose integration
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")


    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // Room (SQLite) - persistencia local estructurada
    implementation("androidx.room:room-runtime:2.6.1")
    // KSP will be used instead of KAPT for Room's annotation processor:
    ksp("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")

    // DataStore (para prefs: notificaciones, hora, flags)
    implementation("androidx.datastore:datastore-preferences:1.1.0")

    // Jetpack Security para almacenamiento encriptado de credenciales/flags sensibles
    implementation("androidx.security:security-crypto:1.1.0-alpha04")

    // Biometría (huella / Face ID)
    implementation("androidx.biometric:biometric:1.2.0-alpha03")

    // WorkManager (para notificaciones programadas y tareas en background)
    implementation("androidx.work:work-runtime-ktx:2.8.1")

    // Notificaciones / compat
    implementation("androidx.core:core-ktx:1.12.0")


    // Red y scraping: OkHttp + Jsoup
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("org.jsoup:jsoup:1.16.1")

    // Carga de imágenes en Compose
    implementation("io.coil-kt:coil-compose:2.4.0")

    // Google Play Billing (para suscripción premium) - opcional hasta que implementes facturación
    implementation("com.android.billingclient:billing-ktx:6.0.1")

    // Accompanist utilities (si vas a usar controlador de sistema UI o permisos en Compose)
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.32.0")
    implementation("com.google.accompanist:accompanist-permissions:0.32.0")

    // Testing / Debug (opcional)
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.04.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
}