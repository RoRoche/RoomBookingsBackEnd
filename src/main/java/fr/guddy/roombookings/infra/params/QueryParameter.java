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
package fr.guddy.roombookings.infra.params;

import io.javalin.http.Context;

/**
 * Parameter passed in the query of the HTTP request.
 *
 * @since 1.0.0
 */
public final class QueryParameter implements Parameter<String> {

    /**
     * The wrapped {@link Parameter} with {@link String} value.
     */
    private final Parameter<String> delegate;

    public QueryParameter(final Parameter<String> delegate) {
        this.delegate = delegate;
    }

    public QueryParameter(final String name, final Context context) {
        this(new StringParameter(name, context.queryParam(name)));
    }

    @Override
    public String name() {
        return this.delegate.name();
    }

    @Override
    public String value() {
        return this.delegate.value();
    }
}
