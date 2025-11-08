package fr.guddy.roombookings.architecture.rules.conditions;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.core.domain.JavaModifier;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import fr.guddy.roombookings.architecture.rules.conditions.messages.PublicMethodsDeclaredInInterfacesMessage;

public final class HaveOnlyPublicMethodsDeclaredInInterfaces extends ArchCondition<JavaClass> {
    public HaveOnlyPublicMethodsDeclaredInInterfaces() {
        super("have only public methods declared in implemented interfaces");
    }

    @Override
    public void check(final JavaClass clazz, final ConditionEvents events) {
        if (clazz.isInterface()) return;

        for (final JavaMethod method : clazz.getMethods()) {
            if (!method.getModifiers().contains(JavaModifier.PUBLIC)) continue;

            final String name = method.getName();
            // Check if this is the public static void main(String[] args) method
            final boolean isMainMethod = new IsMainMethod(method).value();

            // Ignore toString, equals, hashCode, and main
            if (name.equals("toString") || name.equals("equals") || name.equals("hashCode") || isMainMethod) {
                continue;
            }

            if (!new IsDeclaredInInterfaces(method, new InterfaceMethods(clazz)).value()) {
                events.add(
                        SimpleConditionEvent.violated(
                                method,
                                new PublicMethodsDeclaredInInterfacesMessage(clazz, method).toString()
                        )
                );
            }
        }
    }
}
