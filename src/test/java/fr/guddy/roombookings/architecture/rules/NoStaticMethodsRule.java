package fr.guddy.roombookings.architecture.rules;

import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import fr.guddy.roombookings.architecture.rules.conditions.NoStaticMethodsCondition;

public final class NoStaticMethodsRule extends ArchRuleEnvelope {
    public NoStaticMethodsRule() {
        super(
                ArchRuleDefinition.classes().should(new NoStaticMethodsCondition())
        );
    }
}
