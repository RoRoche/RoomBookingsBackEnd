package fr.guddy.roombookings.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import fr.guddy.roombookings.architecture.rules.*;
import org.junit.jupiter.api.Test;

final class ArchitectureTest {

  private final JavaClasses classes = new ClassFileImporter()
    .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
    .importPackages("fr.guddy.roombookings");

  @Test
  void domainClassesAreNotAccessInfra() {
    final ArchRule rule = ArchRuleDefinition.noClasses()
      .that()
      .resideInAPackage("..domain..")
      .should()
      .accessClassesThat()
      .resideInAPackage("..infra..")
      .because("domain logic must not be dependent of infrastructure code");
    rule.check(classes);
  }

  @Test
  void publicMethodsAreDeclaredInInterface() {
    new PublicMethodsDeclaredInInterfacesRule().check(classes);
  }

  @Test
  void classesAreAbstractOrFinal() {
    new ClassesAreAbstractOrFinalRule().check(classes);
  }

  @Test
  void fieldsAreFinal() {
    new FieldsShouldBeFinalRule().check(classes);
  }

  @Test
  void noStaticMethods() {
    new ClassesShouldHaveNoStaticMethodsRule().check(classes);
  }

  @Test
  void classesDoNotHavePrivateMethods() {
    new ClassesShouldNotHavePrivateMethodsRule().check(classes);
  }

  @Test
  void classesDoNotHaveGettersOrSetters() {
    new ClassesShouldNotHaveGettersOrSettersRule().check(classes);
  }
}
