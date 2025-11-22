package fr.guddy.roombookings.architecture.rules;

import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import fr.guddy.roombookings.architecture.rules.conditions.NotHaveGettersOrSetters;

public final class ClassesShouldNotHaveGettersOrSettersRule extends ArchRuleEnvelope {
  public ClassesShouldNotHaveGettersOrSettersRule() {
    super(
      ArchRuleDefinition.classes()
        .should(new NotHaveGettersOrSetters())
        .because("https://www.yegor256.com/2014/09/16/getters-and-setters-are-evil.html")
    );
  }
}
