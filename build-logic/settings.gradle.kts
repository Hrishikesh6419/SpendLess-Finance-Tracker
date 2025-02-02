dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }

    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

rootProject.name = "build-logic" //This should be changed to build-logic since this is a project in itself
include(":convention") // In order to recognize convention as a module we need to give it here