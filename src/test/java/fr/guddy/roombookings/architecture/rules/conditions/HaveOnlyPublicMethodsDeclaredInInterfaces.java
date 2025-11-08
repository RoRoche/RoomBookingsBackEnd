package fr.guddy.roombookings.architecture.rules.conditions;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaModifier;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import fr.guddy.roombookings.architecture.rules.conditions.collections.InterfaceMethods;
import fr.guddy.roombookings.architecture.rules.conditions.messages.PublicMethodsDeclaredInInterfacesMessage;
import fr.guddy.roombookings.architecture.rules.conditions.predicates.IsDeclaredInInterfaces;
import fr.guddy.roombookings.architecture.rules.conditions.predicates.IsMainMethod;
import fr.guddy.roombookings.architecture.rules.conditions.predicates.IsObjectMethod;

public final class HaveOnlyPublicMethodsDeclaredInInterfaces extends ArchCondition<JavaClass> {
    public HaveOnlyPublicMethodsDeclaredInInterfaces() {
        super("have only public methods declared in implemented interfaces");
    }

    @Override
    public void check(final JavaClass clazz, final ConditionEvents events) {
        if (clazz.isInterface()) return;
        clazz.getMethods().stream()
                .filter(method -> method.getModifiers().contains(JavaModifier.PUBLIC))
                .filter(method -> !new IsObjectMethod(method).value() && !new IsMainMethod(method).value())
                .filter(method -> !new IsDeclaredInInterfaces(method, new InterfaceMethods(clazz)).value())
                .forEach(method -> events.add(
                        SimpleConditionEvent.violated(
                                method,
                                new PublicMethodsDeclaredInInterfacesMessage(clazz, method).toString()
                        )
                ));
    }
}
