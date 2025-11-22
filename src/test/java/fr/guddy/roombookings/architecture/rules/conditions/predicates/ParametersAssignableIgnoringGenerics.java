package fr.guddy.roombookings.architecture.rules.conditions.predicates;

import com.tngtech.archunit.core.domain.JavaMethod;
import org.cactoos.Scalar;

import java.util.stream.IntStream;

/**
 * Compare parameters ignoring generics, and check that
 * each implementation type is the same or a subtype of the interface type.
 */
public final class ParametersAssignableIgnoringGenerics implements Scalar<Boolean> {
  private final JavaMethod ifaceMethod;
  private final JavaMethod implMethod;

  public ParametersAssignableIgnoringGenerics(final JavaMethod ifaceMethod, final JavaMethod implMethod) {
    this.ifaceMethod = ifaceMethod;
    this.implMethod = implMethod;
  }

  @Override
  public Boolean value() {
    final var ifaceParams = this.ifaceMethod.getRawParameterTypes();
    final var implParams = this.implMethod.getRawParameterTypes();

    return IntStream.range(0, ifaceParams.size())
      .allMatch(i -> {
        final var ifaceParam = ifaceParams.get(i).toErasure();
        final var implParam = implParams.get(i).toErasure();
        return new IsSameOrSubtype(implParam, ifaceParam).value();
      });
  }
}
