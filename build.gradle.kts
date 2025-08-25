import org.jetbrains.kotlin.gradle.tasks.KotlinCompile



plugins {
  
  val kotlinV = "2.2.10"
  
  
  kotlin("jvm") version kotlinV

  // https://github.com/Kotlin/kotlinx.serialization
  // JSON serialization plugin
  // same as Kotlin version
  kotlin("plugin.serialization") version kotlinV

  application
}


group = "com.rrain.testkotlin"
version = "0.0.1"


kotlin {
  jvmToolchain(21)
}

application {
  mainClass = "MainKt"
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.compilerOptions {
  freeCompilerArgs.add("-Xcontext-parameters") // enable experimental context parameters
}


repositories {
  mavenCentral()
  //gradlePluginPortal()
}


dependencies {
  testImplementation(kotlin("test"))

  // https://mvnrepository.com/artifact/org.jetbrains.kotlinx/kotlinx-coroutines-core
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
  
  // https://mvnrepository.com/artifact/org.jetbrains.kotlinx/kotlinx-datetime
  implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.1")

  // https://mvnrepository.com/artifact/org.jetbrains.kotlinx/kotlinx-serialization-json-jvm
  // https://github.com/Kotlin/kotlinx.serialization
  // JSON serialization
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.1")

  
  // Guava collections
  // https://mvnrepository.com/artifact/com.google.guava/guava
  implementation("com.google.guava:guava:33.4.8-jre")
  
  // Caffeine cache
  // https://mvnrepository.com/artifact/com.github.ben-manes.caffeine/caffeine
  implementation("com.github.ben-manes.caffeine:caffeine:3.2.2")
  // Caffeine-coroutines
  // https://mvnrepository.com/artifact/dev.hsbrysk/caffeine-coroutines
  implementation("dev.hsbrysk:caffeine-coroutines:2.0.2")
  
  
  
  // https://central.sonatype.com/artifact/io.github.xn32/json5k
  // https://github.com/xn32/json5k
  // Дополнение стандарта JSON5
  // https://json5.org/ - стандарт JSON5
  implementation("io.github.xn32:json5k:0.3.0")

  // https://mvnrepository.com/artifact/org.apache.poi/poi
  implementation("org.apache.poi:poi:5.2.4")
  implementation("org.apache.poi:poi-ooxml:5.2.4")

  // https://mvnrepository.com/artifact/org.jsoup/jsoup
  // html & xml parse & work
  implementation("org.jsoup:jsoup:1.16.2")

  // Apache Log4j
  // https://mvnrepository.com/artifact/org.apache.logging.log4j/log4j-core
  implementation("org.apache.logging.log4j:log4j-core:2.21.1")
  implementation(kotlin("reflect"))
  
  // JJWT
  // https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt-api
  implementation("io.jsonwebtoken:jjwt-api:0.12.6")
  
  implementation("org.bouncycastle:bcprov-jdk18on:1.78.1")
  
  
  val jacksonV = "2.18.2"
  implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonV")
  // Kotlin Jackson Support
  // https://github.com/FasterXML/jackson-module-kotlin
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonV")
  // Java Time Jackson Support
  // https://mvnrepository.com/artifact/com.fasterxml.jackson.datatype/jackson-datatype-jsr310
  implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonV")
  
  


  // Annotation Processing:
  //compile("de.jensklingenberg:mpapt-runtime:0.8.7")
  //implementation(project(":annotations"))
  // Code generation library for kotlin, highly recommended
  //implementation("com.squareup:kotlinpoet:0.7.0")
  // configuration generator for service providers
  //implementation("com.google.auto.service:auto-service:1.0-rc4")
  //kapt("com.google.auto.service:auto-service:1.0-rc4")

  // Kotlin Symbol Processing
  // view new versions: https://search.maven.org/artifact/com.google.devtools.ksp/symbol-processing-api
  //implementation("com.google.devtools.ksp:symbol-processing-api:1.6.21-1.0.5")


  //implementation(kotlin("stdlib"))
  //implementation("com.squareup:javapoet:1.13.0")
  // Kotlin Symbol Processing
  // view new versions: https://search.maven.org/artifact/com.google.devtools.ksp/symbol-processing-api
  //implementation("com.google.devtools.ksp:symbol-processing-api:1.6.21-1.0.5")
}

tasks.test {
  useJUnitPlatform()
}