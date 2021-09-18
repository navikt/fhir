import com.github.gradle.node.npm.task.NpxTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("org.jlleitschuh.gradle.ktlint")
    id("com.github.node-gradle.node")
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "11"
    }
    register<NpxTask>("sushi") {
        command.set("fsh-sushi")
    }
    withType<Test> {
        useJUnitPlatform()
        testLogging {
            showStandardStreams = true
        }
    }
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("ca.uhn.hapi.fhir:org.hl7.fhir.validation:5.5.3")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.8.0")
    testImplementation("com.sksamuel.hoplite:hoplite-json:1.4.7")
    testRuntimeOnly("ch.qos.logback:logback-classic:1.2.6")
    testRuntimeOnly("com.squareup.okhttp3:okhttp:4.9.1")
}
