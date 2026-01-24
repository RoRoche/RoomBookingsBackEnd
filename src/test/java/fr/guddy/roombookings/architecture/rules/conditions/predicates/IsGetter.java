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

/**
 * Check if a method is a getter.
 *
 * @since 1.0.0
 */
public final class IsGetter implements Scalar<Boolean> {

    /**
     * The {@link JavaMethod} to test.
     */
    private final JavaMethod method;

    public IsGetter(final JavaMethod method) {
        this.method = method;
    }

    @Override
    public Boolean value() {
        final boolean result;
        if (this.method.reflect().isSynthetic()) {
            result = false;
        } else {
            final String name = this.method.getName();
            final List<JavaClass> params = this.method.getRawParameterTypes();
            final JavaClass returned = this.method.getRawReturnType();
            result =
                isGet(name, params, returned)
                    ||
                    isIs(name, params, returned);
        }
        return result;
    }

    private static boolean isGet(
        final String name,
        final List<JavaClass> params,
        final JavaClass returned
    ) {
        return name.startsWith("get")
            &&
            !name.equals("getClass")
            &&
            params.isEmpty()
            &&
            !returned.isEquivalentTo(void.class);
    }

    private static boolean isIs(
        final String name,
        final List<JavaClass> params,
        final JavaClass returned
    ) {
        return name.startsWith("is")
            &&
            params.isEmpty()
            &&
            (returned.isEquivalentTo(boolean.class)
                ||
                returned.isEquivalentTo(Boolean.class));
    }
}
