plugins {
    alias(libs.plugins.spendless.jvm.library)
}

dependencies {
    implementation(projects.core.domain)
}
