plugins {
    alias(libs.plugins.spendless.android.library)
}

android {
    namespace = "com.spendless.session_management.data"
}

dependencies {
    implementation(libs.bundles.koin)

    implementation(projects.core.domain)
    implementation(projects.sessionManagement.domain)
    implementation(libs.androidx.datastore.preference)
    implementation(libs.kotlinx.coroutines.core)
}