plugins {
    kotlin("jvm") version "1.5.30" apply false
    kotlin("plugin.serialization") version "1.5.30" apply false
    id("org.jlleitschuh.gradle.ktlint") version "10.2.0" apply false
    id("com.github.node-gradle.node") version "3.1.1" apply false
}

subprojects {
    repositories {
        maven("https://github-package-registry-mirror.gc.nav.no/cached/maven-release")
        mavenCentral()
    }
}
