package freemind

import com.tngtech.archunit.base.DescribedPredicate
import com.tngtech.archunit.core.domain.Dependency
import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import com.tngtech.archunit.library.plantuml.PlantUmlArchCondition.Configurations.consideringAllDependencies
import com.tngtech.archunit.library.plantuml.PlantUmlArchCondition.adhereToPlantUmlDiagram
import java.net.URL
import kotlin.test.Test
import kotlin.test.assertNotNull

@Suppress("INACCESSIBLE_TYPE")
class ArchitectureTest() {

    @Test
    fun testArchitectureByPlantUml() {
        val classes = ClassFileImporter().withImportOption(DoNotIncludeTests()).importPackages("freemind")

        val componentDiagram: URL = javaClass.classLoader.getResource("Freemind.puml")
        assertNotNull(componentDiagram, "Looks like the PlantUML file was not found...")

        classes().should(
            adhereToPlantUmlDiagram(componentDiagram, consideringAllDependencies())
                .ignoreDependencies(object : DescribedPredicate<Dependency>("Dependencies to standard libs") {
                    override fun apply(dependency: Dependency?): Boolean =
                        dependency?.targetClass?.packageName?.startsWith("java.") ?: false ||
                            dependency?.targetClass?.packageName == "kotlin" ||
                            dependency?.targetClass?.name?.startsWith("[C") ?: false ||
                            dependency?.targetClass?.name?.startsWith("[F") ?: false ||
                            dependency?.targetClass?.name?.startsWith("[B") ?: false ||
                            dependency?.targetClass?.name?.startsWith("[I") ?: false ||
                            dependency?.targetClass?.name?.startsWith("[D") ?: false
                })
        ).check(classes)
    }
}
