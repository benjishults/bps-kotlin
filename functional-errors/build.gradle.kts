plugins {
    alias(libs.plugins.kotlinJvm)
    id("org.jetbrains.dokka") version "2.1.0"
    `maven-publish`
    `java-library`
}

group = "bps"
version = "1.0-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()
}

kotlin {
    jvmToolchain(25)
}

tasks.named("compileKotlin", org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask::class.java) {
    compilerOptions {
//        freeCompilerArgs.add("-Xcontext-receivers")
        apiVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_3)
    }
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/benjishults/bps-kotlin")
            credentials {
                username = providers
                    .gradleProperty("github.actor")
                    .getOrElse(System.getenv("GITHUB_ACTOR"))
                password = providers
                    .gradleProperty("github.token")
                    .getOrElse(System.getenv("GITHUB_TOKEN"))
            }
        }
    }
    publications {
        create<MavenPublication>("GitHubPackages") {
            groupId = "io.github.benjishults"
            artifactId = "functional-errors"
            from(components["java"])
            pom {
                name = "BPS Functional Errors"
                description = "Help errors without non-local exits."
                url = "https://github.com/benjishults/bps-kotlin"
                licenses {
                    license {
                        name = "The Apache License, Version 2.0"
                        url = "http://www.apache.org/licenses/LICENSE-2.0.txt"
                    }
                }
            }
        }
    }
}

dependencies {

    testImplementation(libs.mockk.jvm)
    testImplementation(libs.kotest.junit5)
    testImplementation(libs.kotest.assertions.core)
    testImplementation(libs.junit.jupiter)
}

tasks.test {
    useJUnitPlatform()
}
