package fr.guddy.roombookings.architecture.rules;

import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import fr.guddy.roombookings.architecture.rules.conditions.NotHavePrivateMethods;

public final class ClassesShouldNotHavePrivateMethodsRule extends ArchRuleEnvelope {
    public ClassesShouldNotHavePrivateMethodsRule() {
        super(
                ArchRuleDefinition.classes().should(new NotHavePrivateMethods())
        );
    }
}
