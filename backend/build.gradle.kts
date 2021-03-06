val ktorVersion = "1.3.2"

plugins {
    application
    java
    kotlin("jvm") version "1.3.72"
    id("com.github.johnrengelman.shadow") version "6.0.0"
    id("org.jlleitschuh.gradle.ktlint") version "9.3.0"
    id("org.jlleitschuh.gradle.ktlint-idea") version "9.3.0"
}

group = "org.gridiron"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.6.2")
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-jackson:$ktorVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-joda:2.11.1")
    implementation("io.ktor:ktor-auth-jwt:$ktorVersion")
    implementation("org.mariadb.jdbc:mariadb-java-client:2.6.2")
    implementation("org.jetbrains.exposed:exposed-core:0.26.1")
    implementation("org.jetbrains.exposed:exposed-dao:0.26.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.26.1")
    implementation("org.jetbrains.exposed:exposed-jodatime:0.26.1")
    implementation("org.jetbrains.exposed:exposed-java-time:0.26.1")
    implementation("com.zaxxer:HikariCP:3.4.5")
    implementation("ch.qos.logback:logback-classic:1.2.3")
    implementation("org.mindrot:jbcrypt:0.4")
    implementation("io.ktor:ktor-client-apache:$ktorVersion")
    implementation("org.mnode.ical4j:ical4j:3.0.19")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }

    test {
        useJUnitPlatform()
        testLogging {
            events("skipped", "failed")
        }
    }
}

application {
    mainClassName = "io.ktor.server.netty.EngineMain"
}

tasks.withType<Jar> {
    archiveFileName.set("gridiron.jar")
    manifest {
        attributes(
            mapOf(
                "Main-Class" to application.mainClassName
            )
        )
    }
}
