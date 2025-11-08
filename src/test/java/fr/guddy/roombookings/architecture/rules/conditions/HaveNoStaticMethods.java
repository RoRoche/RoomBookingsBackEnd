package fr.guddy.roombookings.architecture.rules.conditions;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.core.domain.JavaModifier;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import fr.guddy.roombookings.architecture.rules.conditions.messages.NoStaticMethodsMessage;

public final class HaveNoStaticMethods extends ArchCondition<JavaClass> {
    public HaveNoStaticMethods() {
        super("not have static methods");
    }

    @Override
    public void check(final JavaClass javaClass, final ConditionEvents events) {
        for (final JavaMethod method : javaClass.getMethods()) {
            final String name = method.getName();
            final boolean isAllowedStaticMethod = name.startsWith("$")
                    || method.reflect().isSynthetic();
            if (method.getModifiers().contains(JavaModifier.STATIC) && !new IsMainMethod(method).value() && !isAllowedStaticMethod) {
                events.add(
                        SimpleConditionEvent.violated(
                                method,
                                new NoStaticMethodsMessage(javaClass, method).toString()
                        )
                );
            }
        }
    }
}
