package fr.guddy.roombookings.architecture.rules.conditions;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import fr.guddy.roombookings.architecture.rules.conditions.messages.NotHaveGettersOrSettersMessage;
import fr.guddy.roombookings.architecture.rules.conditions.predicates.IsGetter;
import fr.guddy.roombookings.architecture.rules.conditions.predicates.IsSetter;

public final class NotHaveGettersOrSetters extends ArchCondition<JavaClass> {

  public NotHaveGettersOrSetters() {
    super("not have getter or setter methods");
  }

  @Override
  public void check(final JavaClass javaClass, final ConditionEvents events) {
    javaClass
      .getMethods()
      .stream()
      .filter((method) -> new IsGetter(method).value() || new IsSetter(method).value())
      .forEach((method) ->
        events.add(
          SimpleConditionEvent.violated(
            method,
            new NotHaveGettersOrSettersMessage(javaClass, method).toString()
          )
        )
      );
  }
}
