plugins {
    alias(libs.plugins.spendless.android.feature.ui)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.hrishi.auth.apresentation"
}

dependencies {
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.koin.androidx.compose)
    implementation(projects.core.domain)
    implementation(projects.auth.domain)
}