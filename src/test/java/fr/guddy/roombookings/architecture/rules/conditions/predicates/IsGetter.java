package fr.guddy.roombookings.architecture.rules.conditions.predicates;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaMethod;
import org.cactoos.Scalar;

import java.util.List;

public final class IsGetter implements Scalar<Boolean> {
    private final JavaMethod method;

    public IsGetter(final JavaMethod method) {
        this.method = method;
    }

    @Override
    public Boolean value() {
        if (this.method.reflect().isSynthetic()) return false;

        final String name = this.method.getName();
        final List<JavaClass> params = this.method.getRawParameterTypes();
        final JavaClass returnType = this.method.getRawReturnType();

        // getters start with get/is, take 0 params, and return a value (non-void)
        final boolean isGet = name.startsWith("get")
                && !name.equals("getClass") // ignore Object.getClass()
                && params.isEmpty()
                && !returnType.isEquivalentTo(void.class);

        final boolean isIs = name.startsWith("is")
                && params.isEmpty()
                && (returnType.isEquivalentTo(boolean.class) || returnType.isEquivalentTo(Boolean.class));

        return isGet || isIs;
    }
}
