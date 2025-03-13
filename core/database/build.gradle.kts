plugins {
    alias(libs.plugins.spendless.android.library)
    alias(libs.plugins.spendless.android.room)
}

android {
    namespace = "com.spendless.core.database"
}

dependencies {
    implementation(libs.bundles.koin)
    implementation(libs.sqlcipher)
    implementation(libs.androidx.security.crypto)

    // Project dependencies
    implementation(projects.core.domain)
}