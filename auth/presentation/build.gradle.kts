plugins {
    alias(libs.plugins.spendless.android.feature.ui)
}

android {
    namespace = "com.hrishi.auth.apresentation"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.auth.domain)
}