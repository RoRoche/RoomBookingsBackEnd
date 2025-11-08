package fr.guddy.roombookings.architecture.rules;

import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import fr.guddy.roombookings.architecture.rules.conditions.HaveNoStaticMethods;

public final class ClassesShouldHaveNoStaticMethodsRule extends ArchRuleEnvelope {
    public ClassesShouldHaveNoStaticMethodsRule() {
        super(
                ArchRuleDefinition.classes().should(new HaveNoStaticMethods())
        );
    }
}
