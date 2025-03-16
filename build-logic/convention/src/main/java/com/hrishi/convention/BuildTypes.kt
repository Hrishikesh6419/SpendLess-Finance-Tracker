package com.hrishi.convention

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.BuildType
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.DynamicFeatureExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

internal fun Project.configureBuildTypes(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
    extensionType: ExtensionType
) {
    commonExtension.run {
        buildFeatures {
            buildConfig = true
        }

        when (extensionType) {
            ExtensionType.APPLICATION -> {
                extensions.configure<ApplicationExtension> {
                    buildTypes {
                        debug {
                            configureDebugBuildType()
                        }
                        release {
                            configureReleaseBuildType(commonExtension)
                        }
                    }
                }
            }

            ExtensionType.LIBRARY -> {
                extensions.configure<LibraryExtension> {
                    buildTypes {
                        debug {
                            configureDebugBuildType()
                        }
                        release {
                            configureReleaseBuildType(commonExtension)
                        }
                    }
                }
            }

            ExtensionType.DYNAMIC_FEATURE -> {
                extensions.configure<DynamicFeatureExtension> {
                    buildTypes {
                        debug {
                            configureDebugBuildType()
                        }
                        release {
                            configureReleaseBuildType(commonExtension)
                            isMinifyEnabled = false
                        }
                    }
                }
            }
        }
    }
}

private fun BuildType.configureDebugBuildType() {
    buildConfigField("boolean", "IS_DATABASE_ENCRYPTION_ENABLED", "false")
    buildConfigField("boolean", "IS_TRUSTED_TIME_ENABLED", "false")
}

private fun BuildType.configureReleaseBuildType(
    commonExtension: CommonExtension<*, *, *, *, *, *>
) {
    buildConfigField("boolean", "IS_DATABASE_ENCRYPTION_ENABLED", "false")
    buildConfigField("boolean", "IS_TRUSTED_TIME_ENABLED", "false")
    isMinifyEnabled = true
    proguardFiles(
        commonExtension.getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro"
    )
}