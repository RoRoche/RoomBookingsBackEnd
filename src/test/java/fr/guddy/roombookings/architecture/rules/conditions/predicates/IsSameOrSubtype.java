package fr.guddy.roombookings.architecture.rules.conditions.predicates;

import com.tngtech.archunit.core.domain.JavaClass;
import org.cactoos.Scalar;

/**
 * Simulate isAssignableFrom for ArchUnit 1.4.x:
 * returns true if implClass == ifaceClass or implClass inherits/implements ifaceClass.
 */
public final class IsSameOrSubtype implements Scalar<Boolean> {
    private final JavaClass implClass;
    private final JavaClass ifaceClass;

    public IsSameOrSubtype(final JavaClass implClass, final JavaClass ifaceClass) {
        this.implClass = implClass;
        this.ifaceClass = ifaceClass;
    }

    @Override
    public Boolean value() {
        if (this.implClass.equals(this.ifaceClass)) {
            return true;
        }
        if (this.implClass.getAllRawSuperclasses().contains(this.ifaceClass)) {
            return true;
        }
        for (final JavaClass implementedInterface : this.implClass.getAllRawInterfaces()) {
            if (implementedInterface.equals(this.ifaceClass)) {
                return true;
            }
        }
        return false;
    }
}
