package fr.guddy.roombookings.architecture.rules.conditions.predicates;

import com.tngtech.archunit.core.domain.JavaMethod;
import org.cactoos.Scalar;

public final class IsObjectMethod implements Scalar<Boolean> {
    private final JavaMethod method;

    public IsObjectMethod(final JavaMethod method) {
        this.method = method;
    }

    @Override
    public Boolean value() {
        final String name = method.getName();
        return name.equals("toString") || name.equals("equals") || name.equals("hashCode");
    }
}
