package fr.guddy.roombookings.architecture.rules.conditions.predicates;

import com.tngtech.archunit.core.domain.JavaMethod;
import org.cactoos.Scalar;

import java.util.Set;

public final class IsDeclaredInInterfaces implements Scalar<Boolean> {
    private final JavaMethod implMethod;
    private final Set<JavaMethod> interfaceMethods;

    public IsDeclaredInInterfaces(final JavaMethod implMethod, final Set<JavaMethod> interfaceMethods) {
        this.implMethod = implMethod;
        this.interfaceMethods = interfaceMethods;
    }

    @Override
    public Boolean value() {
        // Check if the implementation method matches any method from the interfaces
        for (final JavaMethod ifaceMethod : this.interfaceMethods) {
            if (ifaceMethod.getName().equals(this.implMethod.getName())
                    && new HaveSameParameterCount(ifaceMethod, this.implMethod).value()
                    && new ParametersAssignableIgnoringGenerics(ifaceMethod, this.implMethod).value()) {
                return true;
            }
        }
        return false;
    }
}
