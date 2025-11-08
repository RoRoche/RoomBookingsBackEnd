package fr.guddy.roombookings.architecture.rules.conditions;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaModifier;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import fr.guddy.roombookings.architecture.rules.conditions.messages.ClassesAreAbstractOrFinalMessage;

public final class ClassesAreAbstractOrFinalCondition extends ArchCondition<JavaClass> {
    public ClassesAreAbstractOrFinalCondition() {
        super("be final or abstract");
    }

    @Override
    public void check(final JavaClass javaClass, final ConditionEvents events) {
        final boolean isAbstract = javaClass.getModifiers().contains(JavaModifier.ABSTRACT);
        final boolean isFinal = javaClass.getModifiers().contains(JavaModifier.FINAL);

        if (isAbstract == isFinal) { // must be exactly one of them
            events.add(
                    SimpleConditionEvent.violated(
                            javaClass,
                            new ClassesAreAbstractOrFinalMessage(javaClass, isAbstract, isFinal).toString()
                    )
            );
        }
    }
}
