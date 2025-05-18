plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.tastehavens"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.tastehavens"
        minSdk = 29
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11



    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.firebase.database)
    implementation(libs.firebase.auth)
    implementation(libs.credentials)
    implementation(libs.credentials.play.services.auth)
    implementation(libs.googleid)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.messaging)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(platform("com.google.firebase:firebase-bom:33.13.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation ("com.github.bumptech.glide:glide:4.15.1")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.15.1")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation ("com.github.blackfizz:eazegraph:1.2.2@aar")
    implementation ("com.nineoldandroids:library:2.4.0")
    implementation ("androidx.cardview:cardview:1.0.0")
    implementation ("com.google.android.gms:play-services-wallet:19.3.0")
    implementation ("com.google.pay.button:compose-pay-button:1.0.0")
    implementation ("com.google.android.gms:play-services-wallet:19.4.0")
    implementation ("com.stripe:stripe-java:29.0.0")
    implementation("com.stripe:financial-connections:21.12.0")
    implementation ("androidx.core:core-ktx:1.16.0")
    implementation ("androidx.core:core:1.12.0")
    implementation ("com.google.android.material:material:1.12.0")
    implementation ("com.vipulasri:ticketview:1.1.2")
    implementation ("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation ("com.google.android.material:material:1.6.0")
    implementation ("com.google.firebase:firebase-messaging:23.4.1")
    implementation ("com.google.firebase:firebase-firestore:24.10.3")

}
