plugins {
    id("java")
    id("org.springframework.boot") version "3.2.2"
    id("io.spring.dependency-management") version "1.1.4"
    alias(libs.plugins.androidApplication) apply false
}

group = "org.sportApp"
version = "1.0-SNAPSHOT"


dependencies {
    implementation(enforcedPlatform("org.junit:junit-bom:5.9.1"))
    implementation("org.junit.jupiter:junit-jupiter")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    implementation("org.springframework.boot:spring-boot-starter-web")
    runtimeOnly("com.h2database:h2")
    implementation("org.projectlombok:lombok:1.18.28")
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.9")
    implementation("com.google.code.gson:gson:2.9.1")
    implementation("com.google.guava:guava:33.0.0-jre")
    implementation("org.json:json:20231013")
}

tasks.test {
    useJUnitPlatform()
}
