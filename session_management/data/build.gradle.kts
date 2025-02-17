plugins {
    alias(libs.plugins.spendless.android.library)
    alias(libs.plugins.protobuf)
}

android {
    namespace = "com.spendless.session_management.data"
}

protobuf {
    protoc {
        artifact = libs.protobuf.protoc.get().toString()
    }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                register("java") {
                    option("lite")
                }
                register("kotlin") {
                    option("lite")
                }
            }
        }
    }
}

dependencies {
    implementation(libs.bundles.koin)

    implementation(projects.core.domain)
    implementation(projects.sessionManagement.domain)
    implementation(libs.androidx.datastore.preference)
    implementation(libs.kotlinx.coroutines.core)
    api(libs.protobuf.kotlin.lite)
}


//androidComponents.beforeVariants {
//    android.sourceSets.register(it.name) {
//        val buildDir = layout.buildDirectory.get().asFile
//        java.srcDir(buildDir.resolve("generated/source/proto/${it.name}/java"))
//        kotlin.srcDir(buildDir.resolve("generated/source/proto/${it.name}/kotlin"))
//    }
//}