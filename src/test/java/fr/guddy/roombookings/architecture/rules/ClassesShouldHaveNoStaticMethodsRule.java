package fr.guddy.roombookings.architecture.rules;

import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import fr.guddy.roombookings.architecture.rules.conditions.HaveNoStaticMethods;

public final class ClassesShouldHaveNoStaticMethodsRule extends ArchRuleEnvelope {
  public ClassesShouldHaveNoStaticMethodsRule() {
    super(
      ArchRuleDefinition.classes()
        .should(new HaveNoStaticMethods())
        .because("https://www.yegor256.com/2017/02/07/private-method-is-new-class.html")
    );
  }
}
