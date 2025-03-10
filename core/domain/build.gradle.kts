plugins {
    alias(libs.plugins.spendless.jvm.library)
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    testImplementation(libs.junit.junit)
}
