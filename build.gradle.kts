plugins {
    java
    id("org.springframework.boot") version "4.0.5"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.cedarmeadowmeats"
version = "0.0.8"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

repositories {
    mavenLocal()
    mavenCentral()
}

extra["awssdk"] = "2.42.30"
extra["awsServerlessJavaContainerSpringboot"] = "3.0.1"
extra["testcontainers"] = "1.21.4"

dependencies {
    implementation(platform("software.amazon.awssdk:bom:${property("awssdk")}"))
    implementation("software.amazon.awssdk:dynamodb")
    implementation("software.amazon.awssdk:dynamodb-enhanced")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-devtools")
    implementation("jakarta.xml.bind:jakarta.xml.bind-api:4.0.5")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.21.2")
    implementation("com.google.cloud:google-cloud-recaptchaenterprise:3.85.0")
    implementation("com.amazonaws.serverless:aws-serverless-java-container-springboot4:${property("awsServerlessJavaContainerSpringboot")}")
    developmentOnly("org.springframework.boot:spring-boot-docker-compose")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:junit-jupiter:${property("testcontainers")}")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

//task("awsJar", type = Jar::class) {
//    manifest.attributes["Main-Class"] = "com.cedarmeadowmeats.orderservice.OrderServiceApplication"
//    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
//    from(configurations.runtimeClasspath.get().map { if (it.isDirectory()) it else zipTree(it) })
//    with(tasks.jar.get() as CopySpec)
//}
//
tasks.register<Zip>("packageJar") {
    into("lib") {
        from(tasks.jar)
        from(configurations.runtimeClasspath)
    }
}

tasks.build {
    dependsOn("packageJar")
}
