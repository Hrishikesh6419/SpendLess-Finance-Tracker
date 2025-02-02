plugins {
    alias(libs.plugins.spendless.android.library)
}

dependencies {
    implementation(libs.bundles.koin)

    implementation(projects.core.domain)
    implementation(projects.auth.domain)
}