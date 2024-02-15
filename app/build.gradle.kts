import com.android.builder.model.PROPERTY_SIGNING_KEY_PASSWORD

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.encript_decript"
    compileSdk = 34
    val KEYPASSWORD: String by project
    val IVPASSWORD: String by project
    val ALGORITHM: String by project
    val TRANSFORMATION: String by project


    defaultConfig {
        applicationId = "com.example.encript_decript"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String","KEYPASSWORD",KEYPASSWORD)
        buildConfigField("String","IVPASSWORD",IVPASSWORD)
        buildConfigField("String","ALGORITHM",ALGORITHM)
        buildConfigField("String","TRANSFORMATION",TRANSFORMATION)
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
    buildFeatures{
        buildConfig=true

    }
}


dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation ("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")

//    implementation ("com.github.barteksc:android-pdf-viewer:2.8.2")
//    implementation ("com.github.barteksc:android-pdf-viewer:3.2.0-beta.1")
//
//    implementation ("com.github.barteksc:AndroidPdfViewer:master-SNAPSHOT")
//    implementation ("com.github.barteksc:android-pdf-viewer:3.1.0-beta.1")

    implementation ("com.github.barteksc:android-pdf-viewer:3.2.0-beta.1")
    implementation ("com.github.barteksc:android-pdf-viewer:2.8.2")

    //noinspection GradleCompatible
//    implementation ("com.android.support:support-compat:28.0.0") // Ensure this version matches the one below
//    //noinspection GradleCompatible
//    implementation ("com.android.support:support-core-utils:28.0.0")
//    implementation ("androidx.versionedparcelable:versionedparcelable:1.1.1")
}

