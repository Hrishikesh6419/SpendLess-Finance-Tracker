plugins {
    alias(libs.plugins.spendless.android.library)
    alias(libs.plugins.spendless.android.room)
}

android {
    namespace = "com.spendless.core.database"
}

dependencies {
    implementation(libs.bundles.koin)

    // Project dependencies
    implementation(projects.core.domain)
}