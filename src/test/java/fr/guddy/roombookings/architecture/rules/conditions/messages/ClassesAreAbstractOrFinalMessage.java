package fr.guddy.roombookings.architecture.rules.conditions.messages;

import com.tngtech.archunit.core.domain.JavaClass;
import org.cactoos.text.FormattedText;
import org.cactoos.text.TextEnvelope;

public final class ClassesAreAbstractOrFinalMessage extends TextEnvelope {
    public ClassesAreAbstractOrFinalMessage(final JavaClass javaClass, final boolean isAbstract, final boolean isFinal) {
        super(
                new FormattedText(
                        "Class %s should be either final or abstract (currently: abstract=%s, final=%s)",
                        javaClass.getFullName(),
                        isAbstract,
                        isFinal
                )
        );
    }
}
