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
