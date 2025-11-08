package fr.guddy.roombookings.architecture.rules.conditions.collections;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaMethod;
import org.cactoos.set.SetEnvelope;

import java.util.stream.Collectors;

public final class InterfaceMethods extends SetEnvelope<JavaMethod> {

    public InterfaceMethods(final JavaClass clazz) {
        super(
                new RecursiveInterfaces(clazz).stream()
                        .flatMap(iface -> iface.getMethods().stream())
                        .collect(Collectors.toSet())
        );
    }
}
