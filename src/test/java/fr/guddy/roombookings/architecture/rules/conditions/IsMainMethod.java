package fr.guddy.roombookings.architecture.rules.conditions;

import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.core.domain.JavaModifier;
import org.cactoos.Scalar;

public final class IsMainMethod implements Scalar<Boolean> {
    private final JavaMethod method;

    public IsMainMethod(final JavaMethod method) {
        this.method = method;
    }

    @Override
    public Boolean value() {
        // Check if this is the public static void main(String[] args) method
        return method.getName().equals("main")
                && method.getModifiers().contains(JavaModifier.PUBLIC)
                && method.getModifiers().contains(JavaModifier.STATIC)
                && method.getRawParameterTypes().size() == 1
                && method.getRawParameterTypes().getFirst().getName().equals("[Ljava.lang.String;");
    }
}
