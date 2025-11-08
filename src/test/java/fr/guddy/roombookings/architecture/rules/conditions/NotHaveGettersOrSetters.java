package fr.guddy.roombookings.architecture.rules.conditions;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import fr.guddy.roombookings.architecture.rules.conditions.messages.NotHaveGettersOrSettersMessage;

public final class NotHaveGettersOrSetters extends ArchCondition<JavaClass> {
    public NotHaveGettersOrSetters() {
        super("not have getter or setter methods");
    }

    @Override
    public void check(final JavaClass javaClass, final ConditionEvents events) {
        for (final JavaMethod method : javaClass.getMethods()) {
            if (new IsGetter(method).value() || new IsSetter(method).value()) {
                events.add(
                        SimpleConditionEvent.violated(
                                method,
                                new NotHaveGettersOrSettersMessage(javaClass, method).toString()
                        )
                );
            }
        }
    }
}
