package ch.d4span.freemind;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.library.plantuml.PlantUmlArchCondition.Configurations.consideringAllDependencies;
import static com.tngtech.archunit.library.plantuml.PlantUmlArchCondition.adhereToPlantUmlDiagram;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.Dependency;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests;
import org.junit.jupiter.api.Test;

public class ArchitectureTest {

  @Test
  void testArchitectureByPlantUml() {
    var classes =
        new ClassFileImporter()
            .withImportOption(new DoNotIncludeTests())
            .importPackages("freemind");

    var componentDiagram = this.getClass().getClassLoader().getResource("Freemind.puml");
    assertNotNull(componentDiagram, "Looks like the PlantUML file was not found...");

    classes()
        .should(
            adhereToPlantUmlDiagram(componentDiagram, consideringAllDependencies())
                .ignoreDependencies(
                    new DescribedPredicate<Dependency>("Dependencies to standard libs") {
                      public boolean apply(Dependency dependency) {
                        return dependency.getTargetClass().getPackageName().startsWith("java.")
                            || dependency.getTargetClass().getName().startsWith("[C")
                            || dependency.getTargetClass().getName().startsWith("[F")
                            || dependency.getTargetClass().getName().startsWith("[B")
                            || dependency.getTargetClass().getName().startsWith("[I")
                            || dependency.getTargetClass().getName().startsWith("[D");
                      }
                    }))
        .check(classes);
  }
}
