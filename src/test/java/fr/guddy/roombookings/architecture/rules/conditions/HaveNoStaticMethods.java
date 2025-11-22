package fr.guddy.roombookings.architecture.rules.conditions;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaModifier;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import fr.guddy.roombookings.architecture.rules.conditions.messages.NoStaticMethodsMessage;
import fr.guddy.roombookings.architecture.rules.conditions.predicates.IsAllowedStaticMethod;
import fr.guddy.roombookings.architecture.rules.conditions.predicates.IsMainMethod;

public final class HaveNoStaticMethods extends ArchCondition<JavaClass> {

  public HaveNoStaticMethods() {
    super("not have static methods");
  }

  @Override
  public void check(final JavaClass javaClass, final ConditionEvents events) {
    javaClass
      .getMethods()
      .stream()
      .filter(
        (method) ->
          method.getModifiers().contains(JavaModifier.STATIC) &&
          !new IsMainMethod(method).value() &&
          !new IsAllowedStaticMethod(method).value()
      )
      .forEach((method) ->
        events.add(
          SimpleConditionEvent.violated(
            method,
            new NoStaticMethodsMessage(javaClass, method).toString()
          )
        )
      );
  }
}
