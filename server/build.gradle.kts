plugins {
    id("java")
    id("org.springframework.boot") version("3.2.2")
    id("io.spring.dependency-management") version("1.1.4")
}

group = "org.sportApp"
version = "1.0-SNAPSHOT"

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")

    implementation("org.modelmapper:modelmapper:3.2.0")
    runtimeOnly("org.postgresql:postgresql")
    implementation(libs.gson)

//    compileOnly("org.projectlombok:lombok")
//    annotationProcessor("org.projectlombok:lombok")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
}

tasks.test {
    useJUnitPlatform()
}