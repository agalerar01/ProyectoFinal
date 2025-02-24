plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.proyectofinal"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.proyectofinal"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures{
        viewBinding = true
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(platform("com.google.firebase:firebase-bom:33.8.0"))
    // Firebase Authentication
    implementation("com.google.firebase:firebase-auth")
    implementation ("com.google.android.material:material:1.10.0")
    implementation("androidx.navigation:navigation-fragment:2.8.5")
    implementation("androidx.navigation:navigation-ui:2.8.5")
    implementation("androidx.lifecycle:lifecycle-livedata:2.8.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.8.7")
    implementation("androidx.room:room-runtime:2.5.0")
    annotationProcessor("androidx.room:room-compiler:2.5.0")
    // Firebase BOM (Gestiona automáticamente las versiones de las dependencias de Firebase)
    implementation(platform("com.google.firebase:firebase-bom:33.8.0"))

    // Dependencia de Firestore
    implementation("com.google.firebase:firebase-firestore")

    // Google Sign-In (Fuera de BOM, ya que no es parte de Firebase)
    implementation("com.google.android.gms:play-services-auth:20.7.0")
    implementation ("com.github.bumptech.glide:glide:4.13.2")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.13.2")

}