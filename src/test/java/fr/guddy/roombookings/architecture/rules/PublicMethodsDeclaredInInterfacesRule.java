package fr.guddy.roombookings.architecture.rules;

import fr.guddy.roombookings.architecture.rules.conditions.HaveOnlyPublicMethodsDeclaredInInterfaces;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

public final class PublicMethodsDeclaredInInterfacesRule extends ArchRuleEnvelope {

  public PublicMethodsDeclaredInInterfacesRule() {
    super(
      classes().that().areNotInterfaces()
        .should(new HaveOnlyPublicMethodsDeclaredInInterfaces())
        .because("https://www.yegor256.com/2014/11/20/seven-virtues-of-good-object.html#2-he-works-by-contracts")
    );
  }
}
