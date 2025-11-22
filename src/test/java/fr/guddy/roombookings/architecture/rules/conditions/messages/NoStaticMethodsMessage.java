package fr.guddy.roombookings.architecture.rules.conditions.messages;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaMethod;
import org.cactoos.text.FormattedText;
import org.cactoos.text.TextEnvelope;

public final class NoStaticMethodsMessage extends TextEnvelope {

  public NoStaticMethodsMessage(final JavaClass javaClass, final JavaMethod method) {
    super(
      new FormattedText(
        "Class %s contains static method %s",
        javaClass.getName(),
        method.getFullName()
      )
    );
  }
}
