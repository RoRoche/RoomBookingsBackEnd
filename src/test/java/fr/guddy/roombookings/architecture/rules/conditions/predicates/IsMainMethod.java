package fr.guddy.roombookings.architecture.rules.conditions.predicates;

import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.core.domain.JavaModifier;
import org.cactoos.Scalar;

public final class IsMainMethod implements Scalar<Boolean> {
  private final JavaMethod method;

  public IsMainMethod(final JavaMethod method) {
    this.method = method;
  }

  @Override
  public Boolean value() {
    // Check if this is the public static void main(String[] args) method
    return this.method.getName().equals("main")
      && this.method.getModifiers().contains(JavaModifier.PUBLIC)
      && this.method.getModifiers().contains(JavaModifier.STATIC)
      && this.method.getRawParameterTypes().size() == 1
      && this.method.getRawParameterTypes().getFirst().getName().equals("[Ljava.lang.String;");
  }
}
