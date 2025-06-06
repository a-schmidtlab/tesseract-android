pluginManagement { 
    repositories { 
        google()
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

rootProject.name = "SofaRotator"
include(":app") 