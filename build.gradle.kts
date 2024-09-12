plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.kafka:kafka-clients:3.5.1")
    implementation("ch.qos.logback:logback-classic:1.4.11")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")
    implementation("org.postgresql:postgresql:42.6.0") // Add this line
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("com.sparkjava:spark-core:2.9.3")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")
}

tasks.test {
    useJUnitPlatform()
}