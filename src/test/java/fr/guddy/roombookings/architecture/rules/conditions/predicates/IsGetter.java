/*
 * MIT License
 *
 * Copyright (c) 2018-2025 Romain Rochegude
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package fr.guddy.roombookings.architecture.rules.conditions.predicates;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaMethod;
import java.util.List;
import org.cactoos.Scalar;

public final class IsGetter implements Scalar<Boolean> {

  private final JavaMethod method;

  public IsGetter(final JavaMethod method) {
    this.method = method;
  }

  @Override
  public Boolean value() {
    if (this.method.reflect().isSynthetic()) return false;

    final String name = this.method.getName();
    final List<JavaClass> params = this.method.getRawParameterTypes();
    final JavaClass returnType = this.method.getRawReturnType();

    // getters start with get/is, take 0 params, and return a value (non-void)
    final boolean isGet =
      name.startsWith("get") &&
      !name.equals("getClass") && // ignore Object.getClass()
      params.isEmpty() &&
      !returnType.isEquivalentTo(void.class);

    final boolean isIs =
      name.startsWith("is") &&
      params.isEmpty() &&
      (returnType.isEquivalentTo(boolean.class) || returnType.isEquivalentTo(Boolean.class));

    return isGet || isIs;
  }
}
