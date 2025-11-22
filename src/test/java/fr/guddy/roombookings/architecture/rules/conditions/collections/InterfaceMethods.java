package fr.guddy.roombookings.architecture.rules.conditions.collections;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaMethod;
import java.util.stream.Collectors;
import org.cactoos.set.SetEnvelope;

public final class InterfaceMethods extends SetEnvelope<JavaMethod> {

  public InterfaceMethods(final JavaClass clazz) {
    super(
      new RecursiveInterfaces(clazz)
        .stream()
        .flatMap((iface) -> iface.getMethods().stream())
        .collect(Collectors.toSet())
    );
  }
}
