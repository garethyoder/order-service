plugins {
    java
    id("org.springframework.boot") version "3.4.4"
    id("io.spring.dependency-management") version "1.1.6"
}

group = "com.cedarmeadowmeats"
version = "0.0.5"

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://repo.spring.io/milestone")
    maven("https://repo.spring.io/snapshot")
}

extra["awssdk"] = "2.31.27"
extra["awsServerlessJavaContainerSpringboot"] = "2.1.3"
extra["localstack"] = "1.20.5"

dependencies {
    implementation(platform("software.amazon.awssdk:bom:${property("awssdk")}"))
    implementation("software.amazon.awssdk:dynamodb")
    implementation("software.amazon.awssdk:dynamodb-enhanced")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-docker-compose")
    implementation("org.springframework.boot:spring-boot-devtools")
    implementation("com.amazonaws.serverless:aws-serverless-java-container-springboot3:${property("awsServerlessJavaContainerSpringboot")}")
    developmentOnly("org.springframework.boot:spring-boot-docker-compose")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:localstack:${property("localstack")}")
    testImplementation("org.testcontainers:junit-jupiter")
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
