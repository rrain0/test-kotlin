import org.jetbrains.kotlin.gradle.tasks.KotlinCompile



plugins {
  val kotlinV = "2.0.20"
  
  
  kotlin("jvm") version kotlinV

  // https://github.com/Kotlin/kotlinx.serialization
  // JSON serialization plugin
  // same as Kotlin version
  kotlin("plugin.serialization") version kotlinV

  application
}



group = "com.rrain"
version = "0.0.1"

repositories {
  mavenCentral()
  //gradlePluginPortal()
}


dependencies {
  testImplementation(kotlin("test"))

  // https://mvnrepository.com/artifact/org.jetbrains.kotlinx/kotlinx-coroutines-core
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")

  // https://mvnrepository.com/artifact/org.jetbrains.kotlinx/kotlinx-serialization-json-jvm
  // https://github.com/Kotlin/kotlinx.serialization
  // JSON serialization
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.1")

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

tasks.withType<KotlinCompile> {
  kotlinOptions.jvmTarget = "17"
}

application {
  mainClass.set("MainKt")
}



/*kotlin {
  sourceSets.all {
    languageSettings.apply {
      languageVersion = "1.7"
    }
  }
}*/
/*
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
  languageVersion = "1.7"
}*/
