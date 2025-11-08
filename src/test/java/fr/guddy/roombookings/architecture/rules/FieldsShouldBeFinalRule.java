package fr.guddy.roombookings.architecture.rules;

import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;

public final class FieldsShouldBeFinalRule extends ArchRuleEnvelope {
    public FieldsShouldBeFinalRule() {
        super(
                ArchRuleDefinition.fields()
                        .should().beFinal()
                        .because("all fields should be immutable")
        );
    }
}
