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
package fr.guddy.roombookings.infra.matchers;

import com.mashape.unirest.http.Headers;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import fr.guddy.roombookings.infra.HttpTestCase;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

/**
 * Check if there is a header with the expected value.
 *
 * @since 1.0.0
 */
public class HasHeaderWithValue extends TypeSafeDiagnosingMatcher<HttpTestCase<?>> {

    /**
     * The header name.
     */
    private final String name;

    /**
     * The expected value.
     */
    private final Matcher<? super String> value;

    public HasHeaderWithValue(final String name, final Matcher<? super String> value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public final void describeTo(final Description description) {
        description
            .appendText("HttpResponse with header ")
            .appendValue(this.name)
            .appendText(" having value ")
            .appendDescriptionOf(this.value);
    }

    @Override
    protected final boolean matchesSafely(
        final HttpTestCase<?> testcase,
        final Description mismatch
    ) {
        final HttpResponse<?> response;
        try {
            response = testcase.response();
        } catch (final UnirestException exception) {
            throw new MatcherRuntimeException(exception);
        }
        final Headers headers = response.getHeaders();
        boolean matches = true;
        if (headers.containsKey(this.name)) {
            final String actual = headers.getFirst(this.name);
            if (!this.value.matches(actual)) {
                mismatch
                    .appendText("header ")
                    .appendValue(this.name)
                    .appendText(" value was ")
                    .appendValue(actual);
                matches = false;
            }
        } else {
            mismatch.appendText("header ").appendText(this.name).appendText(" is missing");
            matches = false;
        }
        return matches;
    }
}
