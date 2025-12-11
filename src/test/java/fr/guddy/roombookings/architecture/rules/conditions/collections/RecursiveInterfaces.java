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
package fr.guddy.roombookings.architecture.rules.conditions.collections;

import com.tngtech.archunit.core.domain.JavaClass;
import java.util.HashSet;
import java.util.Set;
import org.cactoos.scalar.Unchecked;
import org.cactoos.set.SetEnvelope;

public final class RecursiveInterfaces extends SetEnvelope<JavaClass> {

  public RecursiveInterfaces(final JavaClass clazz) {
    super(
      new Unchecked<>(() -> {
        final Set<JavaClass> result = new HashSet<>();
        // Recursively collect interfaces implemented by this class
        clazz
          .getInterfaces()
          .forEach((ifaceType) -> {
            final JavaClass iface = ifaceType.toErasure();
            if (result.add(iface)) {
              result.addAll(new RecursiveInterfaces(iface));
            }
          });
        // Also collect interfaces from superclasses
        clazz
          .getSuperclass()
          .ifPresent((superclassType) ->
            result.addAll(new RecursiveInterfaces(superclassType.toErasure()))
          );
        return result;
      })
        .value()
    );
  }
}
