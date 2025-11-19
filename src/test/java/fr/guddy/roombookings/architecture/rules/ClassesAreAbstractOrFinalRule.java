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
                        .because("https://www.yegor256.com/2014/11/20/seven-virtues-of-good-object.html#7-his-class-is-either-final-or-abstract")
        );
    }
}
