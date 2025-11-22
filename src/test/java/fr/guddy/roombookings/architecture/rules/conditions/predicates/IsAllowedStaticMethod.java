package fr.guddy.roombookings.architecture.rules.conditions.predicates;

import com.tngtech.archunit.core.domain.JavaMethod;
import org.cactoos.Scalar;

public final class IsAllowedStaticMethod implements Scalar<Boolean> {
  private final JavaMethod method;

  public IsAllowedStaticMethod(final JavaMethod method) {
    this.method = method;
  }

  @Override
  public Boolean value() {
    return this.method.getName().startsWith("$") || this.method.reflect().isSynthetic();
  }
}
