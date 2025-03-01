plugins {
    alias(libs.plugins.spendless.jvm.library)
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(projects.core.domain)
    api(projects.sessionManagement.domain)
}
