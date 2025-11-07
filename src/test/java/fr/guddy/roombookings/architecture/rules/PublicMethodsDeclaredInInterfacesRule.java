package fr.guddy.roombookings.architecture.rules;

import fr.guddy.roombookings.architecture.rules.conditions.HaveOnlyPublicMethodsDeclaredInInterfacesCondition;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

public final class PublicMethodsDeclaredInInterfacesRule extends ArchRuleEnvelope {

    public PublicMethodsDeclaredInInterfacesRule() {
        super(
                classes().that().areNotInterfaces().should(new HaveOnlyPublicMethodsDeclaredInInterfacesCondition())
        );
    }
}
