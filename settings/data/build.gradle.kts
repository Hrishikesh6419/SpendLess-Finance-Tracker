plugins {
    alias(libs.plugins.spendless.android.library)
}

android {
    namespace = "com.spendless.settings.data"
}

dependencies {
    implementation(libs.bundles.koin)

    implementation(projects.core.domain)
    implementation(projects.settings.domain)
}