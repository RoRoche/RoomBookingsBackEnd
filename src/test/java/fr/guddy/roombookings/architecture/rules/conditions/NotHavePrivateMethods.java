package fr.guddy.roombookings.architecture.rules.conditions;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaModifier;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import fr.guddy.roombookings.architecture.rules.conditions.messages.NotHavePrivateMethodsMessage;

public final class NotHavePrivateMethods extends ArchCondition<JavaClass> {

  public NotHavePrivateMethods() {
    super("not have private methods");
  }

  @Override
  public void check(final JavaClass javaClass, final ConditionEvents events) {
    javaClass
      .getMethods()
      .stream()
      .filter(
        (method) ->
          method.getModifiers().contains(JavaModifier.PRIVATE) && !method.reflect().isSynthetic()
      )
      .forEach((method) ->
        events.add(
          SimpleConditionEvent.violated(
            method,
            new NotHavePrivateMethodsMessage(javaClass, method).toString()
          )
        )
      );
  }
}
