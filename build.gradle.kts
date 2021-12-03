plugins {
    java
}

ant.importBuild("freemind/build.xml")

tasks.compileJava.configure { dependsOn("gen") }

sourceSets {
    main {
        java {
            setSrcDirs(listOf("freemind"))

            include("freemind/**", "accessories/**")
        }
    }
}