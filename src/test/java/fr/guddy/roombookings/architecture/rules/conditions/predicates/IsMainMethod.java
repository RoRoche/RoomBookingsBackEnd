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

import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.core.domain.JavaModifier;
import org.cactoos.Scalar;

/**
 * Check if the {@link JavaMethod} is the main method.
 *
 * @since 1.0.0
 */
public final class IsMainMethod implements Scalar<Boolean> {

    /**
     * The {@link JavaMethod} to test.
     */
    private final JavaMethod method;

    public IsMainMethod(final JavaMethod method) {
        this.method = method;
    }

    @Override
    public Boolean value() {
        return this.isNamedMain()
            && this.hasCorrectModifiers()
            && this.hasCorrectSignature();
    }

    private boolean isNamedMain() {
        return this.method.getName().equals("main");
    }

    private boolean hasCorrectModifiers() {
        return this.method.getModifiers().contains(JavaModifier.PUBLIC)
            && this.method.getModifiers().contains(JavaModifier.STATIC);
    }

    private boolean hasCorrectSignature() {
        return this.method.getRawParameterTypes().size() == 1
            && this.method.getRawParameterTypes().getFirst().getName()
            .equals("[Ljava.lang.String;");
    }
}
