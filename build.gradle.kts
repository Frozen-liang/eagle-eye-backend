import com.google.protobuf.gradle.*
import org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
import org.springframework.boot.gradle.plugin.SpringBootPlugin


/*
 * This file was generated by the Gradle 'init' task.
 */



group = "com.sms.eagle.eye"
version = "1.2.7"
description = "backend"

val checkstyleVersion by extra("8.42")
val versionLombok by extra("1.18.22")
val versionMapstruct by extra("1.4.2.Final")
val springCloudVersion by extra("2021.0.0")
val guavaVersion by extra("31.0.1-jre")
val grpcVersion by extra("1.44.0")
val protocVersion by extra("3.19.4")
val grpcMapStructVersion by extra("1.21")
val mybatisPlusVersion by extra("3.5.1")
val transmittableThreadVersion by extra("2.12.4")
val jasyptVersion by extra("3.0.4")
val powermockVersion by extra("2.0.2")
val hydraClientVersion by extra("1.11.7")
val caffeineVersion by extra("3.0.6")

plugins {
    java
    checkstyle
    jacoco
    idea
    id("com.github.spotbugs") version "4.7.1"
    id("org.springframework.boot") version "2.6.5"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("com.google.protobuf") version "0.8.18"

}

spotbugs {
    ignoreFailures.set(false)
    toolVersion.set("4.5.3")
    showProgress.set(true)
    effort.set(com.github.spotbugs.snom.Effort.MAX)
    reportLevel.set(com.github.spotbugs.snom.Confidence.MEDIUM)
    omitVisitors.addAll(listOf("FindReturnRef", "RuntimeExceptionCapture"))
    maxHeapSize.set("1g")
    excludeFilter.set(file("excludeFilter.xml"))
    sourceSets.add(sourceSets.main.get())
}

allprojects {
    java.sourceCompatibility = JavaVersion.VERSION_11
    java.targetCompatibility = JavaVersion.VERSION_11
}
checkstyle {
    group = "verification"
    toolVersion = checkstyleVersion
    config = resources.text.fromFile("checkstyle.xml", "UTF-8")
    isShowViolations = true
    isIgnoreFailures = false
    maxWarnings = 0


}

jacoco {
    toolVersion = "0.8.7"
}

repositories {
//    mavenLocal()
    mavenCentral()
    maven {
        url = uri("https://maven.aliyun.com/repository/public")
    }
//    maven("https://plugins.gradle.org/m2/")
}

springBoot {
    buildInfo()
}

dependencyManagement {

    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:$springCloudVersion")
        mavenBom("io.awspring.cloud:spring-cloud-aws-dependencies:2.3.0")
    }
}

dependencies {


    runtimeOnly("org.postgresql:postgresql")


    // grpc
    implementation("com.google.protobuf:protobuf-java-util:$protocVersion")
    implementation("io.grpc:grpc-stub:$grpcVersion")
    implementation("io.grpc:grpc-protobuf:$grpcVersion")
    implementation("io.grpc:grpc-netty-shaded:$grpcVersion")
    implementation(platform(SpringBootPlugin.BOM_COORDINATES))
    implementation("org.springframework.boot:spring-boot-starter-web") {
        exclude(module = "spring-boot-starter-tomcat")
    }
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.cloud:spring-cloud-starter-consul-config:")
    implementation("org.springframework.cloud:spring-cloud-starter-vault-config")
    implementation("org.springframework.boot:spring-boot-starter-undertow")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("com.github.ulisesbocchio:jasypt-spring-boot-starter:$jasyptVersion")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
    implementation("io.github.openfeign:feign-jackson")
    implementation(platform("software.amazon.awssdk:bom:2.17.148"))
    implementation("software.amazon.awssdk:auth")
    implementation("software.amazon.awssdk:regions")
    implementation("software.amazon.awssdk:lambda")
    implementation("software.amazon.awssdk:cloudwatchlogs")
    implementation("software.amazon.awssdk:eventbridge")
    implementation("io.awspring.cloud:spring-cloud-starter-aws-messaging")
    implementation("sh.ory.hydra:hydra-client:$hydraClientVersion")
    implementation("com.github.ben-manes.caffeine:caffeine:$caffeineVersion")
//    implementation("software.amazon.awssdk:sqs")
//    implementation("com.amazonaws:amazon-sqs-java-messaging-lib:1.0.4")
    implementation("com.baomidou:mybatis-plus-boot-starter:$mybatisPlusVersion")
    compileOnly("org.projectlombok:lombok:$versionLombok")
    annotationProcessor("org.mapstruct:mapstruct-processor:$versionMapstruct")
    annotationProcessor("org.projectlombok:lombok:$versionLombok")
    annotationProcessor("org.projectlombok:lombok-mapstruct-binding:0.2.0")
    annotationProcessor("no.entur.mapstruct.spi:protobuf-spi-impl:$grpcMapStructVersion")
    compileOnly("org.mapstruct:mapstruct:$versionMapstruct")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    implementation("org.springdoc:springdoc-openapi-ui:1.6.5")
    implementation("com.google.guava:guava:$guavaVersion")
    implementation("org.apache.commons:commons-lang3:3.12.0")
    implementation("commons-io:commons-io:2.11.0")
    implementation("org.apache.commons:commons-collections4:4.4")
    implementation("io.vavr:vavr:0.10.4")
    implementation("com.hubspot.jackson:jackson-datatype-protobuf:0.9.12")
    implementation("com.alibaba:transmittable-thread-local:$transmittableThreadVersion")
    implementation("org.codehaus.groovy:groovy:3.0.8")
    implementation("org.thymeleaf:thymeleaf-spring5")
    implementation("nz.net.ultraq.thymeleaf:thymeleaf-layout-dialect")
    compileOnly("com.github.spotbugs:spotbugs-annotations:${spotbugs.toolVersion.get()}")
    spotbugs("com.github.spotbugs:spotbugs:${spotbugs.toolVersion.get()}")
    implementation("net.logstash.logback:logstash-logback-encoder:7.0.1")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.mockito:mockito-inline:4.3.1")

}

// Temporarily cancel writing to the list through packaging.
//tasks.withType<BootJar> {
//    manifest {
//        attributes("Product-Version" to project.version.toString())
//    }
//}


tasks.checkstyleTest {
    group = "verification"
    enabled = false
}

tasks.checkstyleMain {
    group = "verification"
    sourceSets.add(sourceSets.main.get())


}

tasks.spotbugsTest {
    enabled = false
}

tasks.spotbugsMain {
    group = "verification"
    showStackTraces = true

    reports.register("html")
}




tasks.jacocoTestReport {
    executionData.setFrom(fileTree(buildDir).include("/jacoco/*.exec"))
    classDirectories.setFrom(sourceSets.main.get().output.asFileTree.matching {
        exclude(
            "**/aws/dto/**",
            "**/common/encrypt/**",
            "**/config/**",
            "**/context/**",
            "**/controller/**",
            "**/domain/entity/**",
            "**/event/**",
            "**/model/**",
            "**/request/**",
            "**/response/**",
            "**/nerko/config/**",
            "**/nerko/dto/**",
            "**/nerko/enums/**",
            "**/nerko/exception/**",
            "**/nerko/response/**",
            "**/wecom/config/**",
            "**/wecom/context/**",
            "**/wecom/dto/**",
            "**/wecom/enums/**",
            "**/wecom/exception/**",
            "**/wecom/manager/**",
            "**/wecom/request/**",
            "**/wecom/response/**",
            "**/EagleEyeBackendApplication.class",
            "**/factory/PluginClient.class",
            "**/common/*.class",
            "com/sms/eagle/eye/plugin/v1",
        )
    })
    dependsOn(tasks.test)
    reports {
        xml.required.set(false)
        csv.required.set(true)
    }


}



tasks.jacocoTestCoverageVerification {
    classDirectories.setFrom( classDirectories.files.flatMap {
        fileTree(it) {
            exclude(
                "**/aws/dto/**",
                "**/common/encrypt/**",
                "**/config/**",
                "**/context/**",
                "**/controller/**",
                "**/domain/entity/**",
                "**/event/**",
                "**/model/**",
                "**/request/**",
                "**/response/**",
                "**/nerko/config/**",
                "**/nerko/dto/**",
                "**/nerko/enums/**",
                "**/nerko/exception/**",
                "**/nerko/response/**",
                "**/wecom/config/**",
                "**/wecom/context/**",
                "**/wecom/dto/**",
                "**/wecom/enums/**",
                "**/wecom/exception/**",
                "**/wecom/manager/**",
                "**/wecom/request/**",
                "**/wecom/response/**",
                "**/EagleEyeBackendApplication.class",
                "**/factory/PluginClient.class",
                "**/common/*.class",
                "com/sms/eagle/eye/plugin/v1/**",
            )
        }
    })
    violationRules {
        rule {
            limit {
                counter = "INSTRUCTION"
                value = "COVEREDRATIO"
                minimum = "0.6".toBigDecimal()
            }
        }
    }
}



tasks.test {
    finalizedBy(tasks.jacocoTestReport)
    useJUnitPlatform()
    testLogging {
        lifecycle {
            events = mutableSetOf(FAILED, PASSED, TestLogEvent.SKIPPED)
            exceptionFormat = FULL
            showExceptions = true
            showCauses = true
            showStackTraces = true
            showStandardStreams = true

        }
        info.events = lifecycle.events
        info.exceptionFormat = lifecycle.exceptionFormat
    }
    val failedTests = mutableListOf<TestDescriptor>()
    val skippedTests = mutableListOf<TestDescriptor>()
    addTestListener(object : TestListener {
        override fun beforeSuite(suite: TestDescriptor) {}
        override fun beforeTest(testDescriptor: TestDescriptor) {}
        override fun afterTest(testDescriptor: TestDescriptor, result: TestResult) {
            when (result.resultType) {
                TestResult.ResultType.FAILURE -> failedTests.add(testDescriptor)
                TestResult.ResultType.SKIPPED -> skippedTests.add(testDescriptor)
                else -> Unit
            }
        }

        override fun afterSuite(suite: TestDescriptor, result: TestResult) {
            if (suite.parent == null) { // root suite
                logger.lifecycle("----")
                logger.lifecycle("Test result: ${result.resultType}")
                logger.lifecycle(
                    "Test summary: ${result.testCount} tests, " +
                            "${result.successfulTestCount} succeeded, " +
                            "${result.failedTestCount} failed, " +
                            "${result.skippedTestCount} skipped"
                )
                failedTests.takeIf { it.isNotEmpty() }?.prefixedSummary("\tFailed Tests")
                skippedTests.takeIf { it.isNotEmpty() }?.prefixedSummary("\tSkipped Tests:")
            }
        }

        private infix fun List<TestDescriptor>.prefixedSummary(subject: String) {
            logger.lifecycle(subject)
            forEach { test -> logger.lifecycle("\t\t${test.displayName()}") }
        }

        private fun TestDescriptor.displayName() = parent?.let { "${it.name} - $name" } ?: name
    })
}

tasks.check {
    dependsOn(tasks.jacocoTestCoverageVerification)
}


tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.addAll(listOf("-Xlint:-unchecked", "-Xlint:none", "-nowarn", "-Xlint:-deprecation"))
    options.isWarnings = true
    options.isVerbose = true
    options.isDeprecation = false

}

protobuf {

    protoc {
        artifact = "com.google.protobuf:protoc:$protocVersion"
    }
    plugins {
        id("grpc") {
            //As a codegen plugin to generate Java code
            //Declares which version of Maven Central library to use.
            //However, it is only declared here,
            //This description alone does not "apply" the plugin.
            //To apply it, you need to set generateProtoTasks, which will be described later.
            artifact = "io.grpc:protoc-gen-grpc-java:$grpcVersion"
        }
    }
    // protobuf-gradle-The plugin will generate a task each time you run protoc.
    //You can set what to use as a code generator for this task.
    //In generateProtoTasks, make the settings.
    generateProtoTasks {
        all().forEach {
            //it means a task that is generated each time you run protoc.
            //This task is done with builtins (the code generator that comes with protoc)
            //plugins (a type of code generator combined with protocol)
            //There are two types of properties, and you can set the one you like.
            //Here, as plugins
            //I have two codegen plugins, grpc and grpckt declared above.
            //This setting "applies" these codegen plugins.

            it.plugins {
                id("grpc")
            }
        }
    }

}








