plugins {
    alias(libs.plugins.spendless.android.library)
}

android {
    namespace = "com.spendless.widget.data"
}

dependencies {
    implementation(libs.bundles.koin)

    implementation(projects.core.domain)
    implementation(projects.widget.domain)
}