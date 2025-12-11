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
import fr.guddy.roombookings.infra.HttpTestCase;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public final class HasHeaderWithValue extends TypeSafeDiagnosingMatcher<HttpTestCase<?>> {

  private final String headerName;
  private final Matcher<? super String> valueMatcher;

  public HasHeaderWithValue(final String headerName, final Matcher<? super String> valueMatcher) {
    this.headerName = headerName;
    this.valueMatcher = valueMatcher;
  }

  @Override
  protected boolean matchesSafely(final HttpTestCase<?> testCase, final Description mismatch) {
    try {
      final HttpResponse<?> response = testCase.response();
      final Headers headers = response.getHeaders();
      if (!headers.containsKey(headerName)) {
        return false;
      }
      final String value = headers.getFirst(headerName);
      final boolean matches = valueMatcher.matches(value);
      if (!matches) {
        if (!headers.containsKey(headerName)) {
          mismatch.appendText("header keys were ").appendValue(headers.keySet());
        } else {
          mismatch
            .appendText("header ")
            .appendValue(headerName)
            .appendText(" value was ")
            .appendValue(headers.getFirst(headerName));
        }
      }
      return matches;
    } catch (final Exception e) {
      mismatch.appendText("exception while executing testcase: ").appendText(e.getMessage());
      return false;
    }
  }

  @Override
  public void describeTo(final Description description) {
    description
      .appendText("HttpResponse with header ")
      .appendValue(headerName)
      .appendText(" having value ")
      .appendDescriptionOf(valueMatcher);
  }
}
