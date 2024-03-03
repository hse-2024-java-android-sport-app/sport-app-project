plugins {
    id("java")
    id("org.springframework.boot") version("3.2.2")
    id("io.spring.dependency-management") version("1.1.4")
}

group = "org.sportApp"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation("org.projectlombok:lombok:1.18.28")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.google.code.gson:gson:2.9.1")
    implementation("com.google.guava:guava:33.0.0-jre")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    runtimeOnly("com.h2database:h2")
}

tasks.test {
    useJUnitPlatform()
}