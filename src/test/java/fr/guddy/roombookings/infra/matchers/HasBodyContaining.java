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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import fr.guddy.roombookings.infra.HttpTestCase;
import java.util.Map;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

/**
 * Check if an HTTP request has body containing.
 *
 * @since 1.0.0
 */
public class HasBodyContaining extends TypeSafeDiagnosingMatcher<HttpTestCase<String>> {

    /**
     * The expected JSON entries.
     */
    private final Map<String, Object> expected;

    public HasBodyContaining(final Map<String, Object> expected) {
        this.expected = expected;
    }

    @Override
    public final void describeTo(final Description description) {
        description
            .appendText("HttpResponse body containing body ")
            .appendValue(this.expected);
    }

    @Override
    protected final boolean matchesSafely(
        final HttpTestCase<String> testcase,
        final Description mismatch
    ) {
        final ObjectMapper mapper = new ObjectMapper();
        final HttpResponse<String> response;
        try {
            response = testcase.response();
        } catch (final UnirestException exception) {
            throw new MatcherRuntimeException(exception);
        }
        final Map<String, Object> actual;
        try {
            actual = mapper.readValue(
                response.getBody(),
                new TypeReference<>() {
                }
            );
        } catch (final JsonProcessingException exception) {
            throw new MatcherRuntimeException(exception);
        }
        final Boolean matches = new DeepMatches().apply(actual, this.expected);
        if (!matches) {
            mismatch.appendText("body was ").appendValue(response.getBody());
        }
        return matches;
    }
}
