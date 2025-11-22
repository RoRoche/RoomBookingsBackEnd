package fr.guddy.roombookings.architecture.rules.conditions.messages;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaMethod;
import org.cactoos.text.FormattedText;
import org.cactoos.text.TextEnvelope;

public final class NotHavePrivateMethodsMessage extends TextEnvelope {
  public NotHavePrivateMethodsMessage(final JavaClass javaClass, final JavaMethod method) {
    super(
      new FormattedText(
        "Class %s contains private method %s",
        javaClass.getName(),
        method.getFullName()
      )
    );
  }
}
