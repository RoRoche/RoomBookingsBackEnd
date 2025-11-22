package fr.guddy.roombookings.architecture.rules.conditions.predicates;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaMethod;
import org.cactoos.Scalar;

import java.util.List;

public final class IsSetter implements Scalar<Boolean> {
  private final JavaMethod method;

  public IsSetter(final JavaMethod method) {
    this.method = method;
  }

  @Override
  public Boolean value() {
    if (this.method.reflect().isSynthetic()) return false;

    final String name = this.method.getName();
    final List<JavaClass> params = this.method.getRawParameterTypes();
    final JavaClass returnType = this.method.getRawReturnType();

    // setters start with set, take exactly 1 param, and return void
    return name.startsWith("set")
      && params.size() == 1
      && returnType.isEquivalentTo(void.class);
  }
}
