package fr.guddy.roombookings.architecture.rules.conditions;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaMethod;
import org.cactoos.text.FormattedText;
import org.cactoos.text.TextEnvelope;

public final class PublicMethodsDeclaredInInterfacesMessage extends TextEnvelope {
    public PublicMethodsDeclaredInInterfacesMessage(final JavaClass clazz, final JavaMethod method) {
        super(
                new FormattedText(
                        "The public method %s(%s) in %s is not declared in an interface",
                        method.getName(),
                        method.getRawParameterTypes(),
                        clazz.getFullName()
                )
        );
    }
}
