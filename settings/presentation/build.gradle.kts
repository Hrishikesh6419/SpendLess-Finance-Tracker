plugins {
    alias(libs.plugins.spendless.android.feature.ui)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.spendless.settings.apresentation"
}

dependencies {
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.koin.androidx.compose)

    // Project dependencies
    implementation(projects.core.domain)
    implementation(projects.settings.domain)
    api(projects.sessionManagement.presentation)
}