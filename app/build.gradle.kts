//plugins {
//    alias(libs.plugins.android.application)
//}
//
//android {
//    namespace = "com.example.nhakhoaapp"
//    compileSdk = 35
//
//    defaultConfig {
//        applicationId = "com.example.nhakhoaapp"
//        minSdk = 24
//        targetSdk = 35
//        versionCode = 1
//        versionName = "1.0"
//
//        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//    }
//
//    buildTypes {
//        release {
//            isMinifyEnabled = false
//            proguardFiles(
//                getDefaultProguardFile("proguard-android-optimize.txt"),
//                "proguard-rules.pro"
//            )
//        }
//    }
//    compileOptions {
//        sourceCompatibility = JavaVersion.VERSION_11
//        targetCompatibility = JavaVersion.VERSION_11
//    }
//}
//
//dependencies {
//
//    implementation(libs.appcompat)
//    implementation(libs.material)
//    implementation(libs.activity)
//    implementation(libs.constraintlayout)
//    testImplementation(libs.junit)
//    androidTestImplementation(libs.ext.junit)
//    androidTestImplementation(libs.espresso.core)
//
//    // Retrofit (Cần thiết)
//    implementation("com.squareup.retrofit2:retrofit:2.9.0")
//    // Converter cho JSON (Bắt buộc, nên dùng Gson)
//    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
//}

plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.nhakhoaapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.nhakhoaapp"
        minSdk = 24
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
        // AGP 8.x vẫn chơi được với Java 11, nếu sau này cần thì nâng lên 17
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    // AndroidX & Material từ Version Catalog
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Retrofit (mới 2.11.0)
    implementation("com.squareup.retrofit2:retrofit:2.11.0")

    // Converter cho JSON (Gson)
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

    // RecyclerView để hiển thị danh sách
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // OkHttp + Logging Interceptor (để log request/response)
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
}
