package fr.guddy.roombookings.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import fr.guddy.roombookings.architecture.rules.*;
import org.junit.jupiter.api.Test;

final class ArchitectureTest {
    private static final JavaClasses CLASSES = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("fr.guddy.roombookings");

    @Test
    void testDomainClassesShouldNotAccessInfra() {
        final ArchRule rule = ArchRuleDefinition.noClasses()
                .that().resideInAPackage("..domain..")
                .should().accessClassesThat().resideInAPackage("..infra..")
                .because("domain logic must not be dependent of infrastructure code");
        rule.check(CLASSES);
    }

    @Test
    void testPublicMethodsAreDeclaredInInterface() {
        new PublicMethodsDeclaredInInterfacesRule().check(CLASSES);
    }

    @Test
    void testClassesAreAbstractOrFinal() {
        new ClassesAreAbstractOrFinalRule().check(CLASSES);
    }

    @Test
    void testFieldsShouldBeFinalRule() {
        new FieldsShouldBeFinalRule().check(CLASSES);
    }

    @Test
    void testNoStaticMethodsRule() {
        new ClassesShouldHaveNoStaticMethodsRule().check(CLASSES);
    }

    @Test
    void testClassesShouldNotHavePrivateMethodsRule() {
        new ClassesShouldNotHavePrivateMethodsRule().check(CLASSES);
    }

    @Test
    void testClassesShouldNotHaveGettersOrSettersRule() {
        new ClassesShouldNotHaveGettersOrSettersRule().check(CLASSES);
    }
}
