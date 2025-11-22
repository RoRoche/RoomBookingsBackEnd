package fr.guddy.roombookings.architecture.rules;

import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;

public final class FieldsShouldBeFinalRule extends ArchRuleEnvelope {

  public FieldsShouldBeFinalRule() {
    super(
      ArchRuleDefinition.fields()
        .should()
        .beFinal()
        .because(
          "https://www.yegor256.com/2014/11/20/seven-virtues-of-good-object.html#4-he-is-immutable"
        )
    );
  }
}
