pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            setUrl("https://naver.jfrog.io/artifactory/maven/")
        }
        maven {
            setUrl("https://devrepo.kakao.com/nexus/content/groups/public/")
        }
        maven {
            setUrl("https://jitpack.io")
        }
    }
}
rootProject.name = "TodayRoute"
include(":app")
include(":data")
