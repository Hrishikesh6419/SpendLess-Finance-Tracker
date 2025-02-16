plugins {
    alias(libs.plugins.spendless.android.feature.ui)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.spendless.dashboard.core"
}

dependencies {
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.koin.androidx.compose)

    // Project Dependencies
    implementation(projects.core.domain)
    implementation(projects.dashboard.domain)
    api(projects.sessionManagement.presentation)
}