object Versions {
    const val minSdkVersion = 21
    const val targetSdkVersion = 28
    const val compileSdkVersion = 28
    const val kotlin = "1.3.41"
    const val androidPlugin = "3.4.2"
    const val fabricPlugin = "1.30.0"
    const val playServicesPlugin = "4.3.0"
    const val coroutines = "1.2.2"
    const val ktor = "1.2.2"
}

object Libs {
    const val supportLibVersion = "28.0.0"

    const val support_v4 = "com.android.support:support-v4:$supportLibVersion"
    const val appcompat_v7 = "com.android.support:appcompat-v7:$supportLibVersion"
    const val design = "com.android.support:design:$supportLibVersion"
    const val recyclerview_v7 = "com.android.support:recyclerview-v7:$supportLibVersion"
    const val cardview_v7 = "com.android.support:cardview-v7:$supportLibVersion"
    const val play_auth = "com.google.android.gms:play-services-auth:17.0.0"
    const val play_firebase_core = "com.google.firebase:firebase-core:17.0.1"
    const val play_firebase_messaging = "com.google.firebase:firebase-messaging:19.0.1"

    const val crashlytics = "com.crashlytics.sdk.android:crashlytics:2.10.1"
    const val timber = "com.jakewharton.timber:timber:4.7.1"
    const val rxjava2 = "io.reactivex.rxjava2:rxjava:2.2.10"
    const val junit = "junit:junit:4.12"
    const val espresso = "com.android.support.test.espresso:espresso-core:3.0.2"
    const val espressoIntents = "com.android.support.test.espresso:espresso-intents:3.0.2"
    const val test_runner = "com.android.support.test:runner:1.0.2"
    const val test_rules = "com.android.support.test:rules:1.0.2"
    const val ktx = "androidx.core:core-ktx:1.0.2"
    const val constraintLayout = "com.android.support.constraint:constraint-layout:1.1.3"
    const val paperDb = "io.paperdb:paperdb:2.6"
    const val rxAndroid2 = "io.reactivex.rxjava2:rxandroid:2.1.1"
    const val retrofit = "com.squareup.retrofit2:retrofit:2.6.0"
    const val retrofitCoroutines = "ru.gildor.coroutines:kotlin-coroutines-retrofit:1.1.0"
    const val retrofitRx2 = "com.squareup.retrofit2:adapter-rxjava2:2.6.0"
    const val pngj = "ar.com.hjg:pngj:2.1.0"
    const val okhttp = "com.squareup.okhttp3:okhttp:3.14.2"
    const val okhttpLogging = "com.squareup.okhttp3:logging-interceptor:3.14.2"
    const val multidex = "com.android.support:multidex:1.0.3"
    const val picassoDownloader = "com.jakewharton.picasso:picasso2-okhttp3-downloader:1.1.0"
    const val picasso = "com.squareup.picasso:picasso:2.5.2"
    const val room = "android.arch.persistence.room:runtime:1.1.1"
    const val rxRoom = "android.arch.persistence.room:rxjava2:1.1.1"
    const val roomProcessor = "android.arch.persistence.room:compiler:1.1.1"
    const val flexbox = "com.google.android:flexbox:1.1.0"

    const val stdlibCommon = "org.jetbrains.kotlin:kotlin-stdlib-common"
    const val stdlibJdk8 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8"

    const val corountinesCommon = "org.jetbrains.kotlinx:kotlinx-coroutines-core-common:${Versions.coroutines}"
    const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    const val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
    const val serializationRuntimeCommon = "org.jetbrains.kotlinx:kotlinx-serialization-runtime-common:0.11.0"
    const val serializationRuntime = "org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.11.0"

    const val ktorClientCore = "io.ktor:ktor-client-core:${Versions.ktor}"
    const val ktorClientCoreJvm = "io.ktor:ktor-client-core-jvm:${Versions.ktor}"
    const val ktorClientJson = "io.ktor:ktor-client-json:${Versions.ktor}"
    const val ktorClientJsonJvm = "io.ktor:ktor-client-json-jvm:${Versions.ktor}"
    const val ktorClientSerialization = "io.ktor:ktor-client-serialization:${Versions.ktor}"
    const val ktorClientSerializationJvm = "io.ktor:ktor-client-serialization-jvm:${Versions.ktor}"
    const val ktorClientOkhttp = "io.ktor:ktor-client-okhttp:${Versions.ktor}"
    const val ktorClientEncoding = "io.ktor:ktor-client-encoding:${Versions.ktor}"
    const val ktorClientEncodingJvm = "io.ktor:ktor-client-encoding-jvm:${Versions.ktor}"

    const val kotlinxIo = "org.jetbrains.kotlinx:kotlinx-io:0.1.8"
    const val kotlinxIoJvm = "org.jetbrains.kotlinx:kotlinx-io-jvm:0.1.8"

    const val klock = "com.soywiz.korlibs.klock:klock:1.4.0"

    const val kotlinTestCommon = "org.jetbrains.kotlin:kotlin-test-common"
    const val kotlinTestAnnotationCommon = "org.jetbrains.kotlin:kotlin-test-annotations-common"
    const val kotlinTestJvm = "org.jetbrains.kotlin:kotlin-test-junit"
    const val kotlinTestAnnotationJvm = "org.jetbrains.kotlin:kotlin-test-annotations"
}