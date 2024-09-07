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
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "3days"
include(":app")
include(":core")
include(":core:design-system")
include(":core:utils")
include(":core:network")
include(":core:model")
include(":domain")
include(":data")
include(":feat")
include(":domain:auth")
include(":feat:splash")
