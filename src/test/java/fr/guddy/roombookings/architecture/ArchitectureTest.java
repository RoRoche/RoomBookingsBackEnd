package fr.guddy.roombookings.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.junit.jupiter.api.Test;

public final class ArchitectureTest {
    @Test
    void testDomainClassesShouldNotAccessInfra() {
        final JavaClasses classes = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("fr.guddy.roombookings");
        final ArchRule rule = ArchRuleDefinition.noClasses()
                .that().resideInAPackage("..domain..")
                .should().accessClassesThat().resideInAPackage("..infra..")
                .because("domain logic must not be dependent of infrastructure code");
        rule.check(classes);
    }
}
