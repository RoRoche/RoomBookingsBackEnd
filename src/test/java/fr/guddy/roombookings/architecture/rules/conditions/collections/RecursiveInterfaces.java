package fr.guddy.roombookings.architecture.rules.conditions.collections;

import com.tngtech.archunit.core.domain.JavaClass;
import org.cactoos.scalar.Unchecked;
import org.cactoos.set.SetEnvelope;

import java.util.HashSet;
import java.util.Set;

public final class RecursiveInterfaces extends SetEnvelope<JavaClass> {

    public RecursiveInterfaces(final JavaClass clazz) {
        super(
                new Unchecked<>(() -> {
                    final Set<JavaClass> result = new HashSet<>();
                    // Recursively collect interfaces implemented by this class
                    clazz.getInterfaces().forEach(ifaceType -> {
                        final JavaClass iface = ifaceType.toErasure();
                        if (result.add(iface)) {
                            result.addAll(new RecursiveInterfaces(iface));
                        }
                    });
                    // Also collect interfaces from superclasses
                    clazz.getSuperclass().ifPresent(superclassType ->
                            result.addAll(new RecursiveInterfaces(superclassType.toErasure()))
                    );
                    return result;
                }).value());
    }
}
