package fr.guddy.roombookings;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;

@AnalyzeClasses(packages = "fr.guddy.roombookings")
public final class ArchitectureTest {
    @ArchTest
    public static final ArchRule noDomainClassesShouldAccessToInfraClasses = ArchRuleDefinition.noClasses()
            .that().resideInAPackage("..domain..")
            .should().accessClassesThat().resideInAPackage("..infra..")
            .because("domain logic must not be dependent of infrastructure code");
}
