plugins {
    alias(libs.plugins.spendless.android.feature.ui)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.spendless.session_management.presentation"
}

dependencies {
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.koin.androidx.compose)
    implementation(projects.core.domain)
    implementation(projects.sessionManagement.domain)
}