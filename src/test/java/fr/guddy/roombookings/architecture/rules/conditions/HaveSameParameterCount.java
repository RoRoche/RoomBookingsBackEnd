package fr.guddy.roombookings.architecture.rules.conditions;

import com.tngtech.archunit.core.domain.JavaMethod;
import org.cactoos.Scalar;

public final class HaveSameParameterCount implements Scalar<Boolean> {
    private final JavaMethod a;
    private final JavaMethod b;

    public HaveSameParameterCount(final JavaMethod a, final JavaMethod b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public Boolean value() {
        return this.a.getRawParameterTypes().size() == this.b.getRawParameterTypes().size();
    }
}
