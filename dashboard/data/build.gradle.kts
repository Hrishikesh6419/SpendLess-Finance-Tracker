plugins {
    alias(libs.plugins.spendless.android.library)
}

android {
    namespace = "com.spendless.dashboard.data"
}

dependencies {
    implementation(libs.bundles.koin)

    implementation(projects.core.domain)
    implementation(projects.dashboard.domain)
}