plugins {
    alias(libs.plugins.spendless.android.library)
}

android {
    namespace = "com.hrishi.core.data"
}

dependencies {
    implementation(libs.timber)
    implementation(libs.bundles.koin)
    implementation(projects.core.domain)
    implementation(libs.androidx.security.crypto)
    implementation(projects.core.database)
    implementation(libs.play.services.time)
}