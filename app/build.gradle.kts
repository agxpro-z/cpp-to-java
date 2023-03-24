plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    application
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
}

version = "0.2-alpha"

application {
    // Define the main class for the application.
    mainClass.set("cpp.to.java.Main")
}

tasks.withType<Jar> {
    archiveFileName.set("cpp2java-${version}.jar")
    manifest {
        attributes["Main-Class"] = "cpp.to.java.Main"
    }
}
