pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
        maven { url = uri("https://jitpack.io") } // ✅ Needed for MapLibre plugin dependencies
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS) // ✅ To prefer these over project-level
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
        } // ✅ Corrected placement for MapLibre
    }


rootProject.name = "Campusbites"
include(":app")
