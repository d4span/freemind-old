plugins {
    kotlin("jvm") version "1.6.0"
}

sourceSets {
    main {
        java {
            setSrcDirs(listOf("freemind"))

            include("freemind/**", "accessories/**", "tests/freemind/**", "plugins/**", "de/foltin/**")
            exclude("plugins/collaboration/jabber/**")
        }
    }

    test {
        resources {
            setSrcDirs(listOf("uml"))
        }
    }
}

tasks {
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions {
            allWarningsAsErrors = true
        }
    }
    test {
        useJUnitPlatform()
    }
}

repositories(RepositoryHandler::mavenCentral)

dependencies {
    implementation("org.jibx:jibx-run:1.3.3")
    implementation("org.jsoup:jsoup:1.10.3")
    implementation("com.jgoodies:jgoodies-forms:1.8.0")
    implementation("junit:junit:4.13.2")
    implementation("org.apache.xmlgraphics:batik-svggen:1.14")
    implementation("org.apache.xmlgraphics:batik-transcoder:1.14")
    implementation("org.apache.xmlgraphics:fop-transcoder:2.6")
    implementation("org.apache.lucene:lucene-queryparser:4.6.0")
    implementation("org.apache.lucene:lucene-core:4.6.0")
    implementation("org.apache.lucene:lucene-analyzers-common:4.6.0")
    implementation("javax.help:javahelp:2.0.05")
    implementation("org.hsqldb:hsqldb:2.6.1")

    implementation(files("freemind/lib/jortho.jar",
        "freemind/lib/SimplyHTML/SimplyHTML.jar",
        "freemind/plugins/latex/HotEqn.jar",
        "freemind/plugins/map/JMapViewer.jar",
        "freemind/plugins/script/groovy-all.jar"))

    testImplementation(kotlin("test"))
    testImplementation("com.tngtech.archunit:archunit:0.22.0")
}