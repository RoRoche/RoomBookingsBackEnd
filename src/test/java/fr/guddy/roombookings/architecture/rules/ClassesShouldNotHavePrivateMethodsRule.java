package fr.guddy.roombookings.architecture.rules;

import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import fr.guddy.roombookings.architecture.rules.conditions.NotHavePrivateMethods;

public final class ClassesShouldNotHavePrivateMethodsRule extends ArchRuleEnvelope {

  public ClassesShouldNotHavePrivateMethodsRule() {
    super(
      ArchRuleDefinition.classes()
        .should(new NotHavePrivateMethods())
        .because("https://www.yegor256.com/2017/02/07/private-method-is-new-class.html")
    );
  }
}
