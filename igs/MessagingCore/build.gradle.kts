import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("org.jlleitschuh.gradle.ktlint")
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "16"
    }
    withType<Test> {
        useJUnitPlatform()
    }
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("ca.uhn.hapi.fhir:org.hl7.fhir.validation:5.5.3")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.8.0")
    testRuntimeOnly("ch.qos.logback:logback-classic:1.2.6")
    testRuntimeOnly("com.squareup.okhttp3:okhttp:4.9.1")
}
