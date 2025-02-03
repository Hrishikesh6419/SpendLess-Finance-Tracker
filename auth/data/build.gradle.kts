plugins {
    alias(libs.plugins.spendless.android.library)
}

android {
    namespace = "com.hrishi.auth.data"
}

dependencies {
    implementation(libs.bundles.koin)

    implementation(projects.core.domain)
    implementation(projects.auth.domain)
}