plugins {
    kotlin("jvm") version "1.5.30"
    id("org.jlleitschuh.gradle.ktlint") version "10.2.0"
}

repositories {
    mavenCentral()
}

tasks {
    withType<Test> {
        useJUnitPlatform()
    }
}

dependencies {
    testImplementation("ca.uhn.hapi.fhir:org.hl7.fhir.validation:5.5.3")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.8.0")
    testImplementation("com.sksamuel.hoplite:hoplite-json:1.4.7")
    testRuntimeOnly("ch.qos.logback:logback-classic:1.2.6")
    testRuntimeOnly("com.squareup.okhttp3:okhttp:4.9.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.0")
}
