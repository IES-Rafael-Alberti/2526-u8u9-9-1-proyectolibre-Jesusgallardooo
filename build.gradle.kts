plugins {
    kotlin("jvm") version "2.3.0"
    application
}

group = "org.iesra"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("com.h2database:h2:2.2.224")
    implementation("org.mongodb:mongodb-driver-sync:5.6.4")
    implementation("org.slf4j:slf4j-simple:2.0.13")
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.1")
}

tasks.withType<JavaExec> {
    jvmArgs = listOf(
        "-Djava.net.preferIPv4Stack=true",
        "-Djava.net.preferIPv6Addresses=false",
        "-Djdk.tls.client.protocols=TLSv1.2"
    )
    standardInput = System.`in`
}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("app.MainKt")
}