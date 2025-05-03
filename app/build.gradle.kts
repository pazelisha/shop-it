plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.shopit"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.shopit"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true  // Added for MongoDB support

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
        // Configure desugaring for Java 8+ features
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    packaging {
        resources {
            excludes += listOf(
                "META-INF/INDEX.LIST",
                "META-INF/DEPENDENCIES",
                "META-INF/LICENSE",
                "META-INF/LICENSE.txt",
                "META-INF/license.txt",
                "META-INF/NOTICE",
                "META-INF/NOTICE.txt",
                "META-INF/notice.txt",
                "META-INF/ASL2.0",
                "META-INF/*.kotlin_module"
            )
        }
    }
}

dependencies {
    // AndroidX Dependencies
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.multidex:multidex:2.0.1")  // Keep for MultiDex support

    // MongoDB dependencies - Updated version, simplified
    // mongodb-driver-sync should bring in bson and mongodb-driver-core transitively
    implementation("org.mongodb:mongodb-driver-sync:4.11.1") // <-- Updated Version (Example, check latest 4.x)

    // Add desugaring library for Java 8+ features
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.3") // Keep for Java 8+ API desugaring

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}