package fr.guddy.roombookings.architecture.rules;

import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import fr.guddy.roombookings.architecture.rules.conditions.BeAbstractOrFinal;

public final class ClassesAreAbstractOrFinalRule extends ArchRuleEnvelope {
    public ClassesAreAbstractOrFinalRule() {
        super(
                ArchRuleDefinition.classes()
                        .that().areNotInterfaces()
                        .and().areNotEnums()
                        .should(new BeAbstractOrFinal())
                        .because("every class should be either final or abstract to make the design intention explicit")
        );
    }
}
