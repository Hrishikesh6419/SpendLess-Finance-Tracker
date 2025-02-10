plugins {
    alias(libs.plugins.spendless.android.library.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.hrishi.presentation.ui"
}

dependencies {
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    // Local module dependencies
    implementation(projects.core.domain)
    implementation(projects.core.presentation.designsystem)
}