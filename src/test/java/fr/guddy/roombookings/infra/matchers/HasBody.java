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

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import fr.guddy.roombookings.infra.HttpTestCase;
import org.cactoos.Text;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

/**
 * Check if an HTTP request has a body.
 *
 * @since 1.0.0
 */
public class HasBody extends TypeSafeDiagnosingMatcher<HttpTestCase<String>> {

    /**
     * The expected body.
     */
    private final String expected;

    public HasBody(final String expected) {
        this.expected = expected;
    }

    public HasBody(final Text text) {
        this(text.toString());
    }

    @Override
    public final void describeTo(final Description description) {
        description.appendText("an HttpResponse with body ").appendValue(this.expected);
    }

    @Override
    protected final boolean matchesSafely(
        final HttpTestCase<String> testcase,
        final Description mismatch
    ) {
        final HttpResponse<String> response;
        try {
            response = testcase.response();
        } catch (final UnirestException exception) {
            throw new MatcherRuntimeException(exception);
        }
        final String actual = response.getBody();
        final boolean matches = this.expected.equalsIgnoreCase(actual);
        if (!matches) {
            mismatch.appendText("was HttpResponse with body ").appendValue(actual);
        }
        return matches;
    }
}
