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
import fr.guddy.roombookings.infra.HttpTestCase;
import org.cactoos.Text;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public final class HasBody extends TypeSafeDiagnosingMatcher<HttpTestCase<String>> {

  private final String expectedBody;

  public HasBody(final String expectedBody) {
    this.expectedBody = expectedBody;
  }

  public HasBody(final Text text) {
    this(text.toString());
  }

  @Override
  protected boolean matchesSafely(final HttpTestCase<String> testCase, final Description mismatch) {
    try {
      final HttpResponse<String> response = testCase.response();
      final String actualBody = response.getBody();
      final boolean matches = expectedBody.equalsIgnoreCase(actualBody);
      if (!matches) {
        mismatch.appendText("was HttpResponse with body ").appendValue(actualBody);
      }
      return matches;
    } catch (Exception e) {
      mismatch.appendText("exception while executing testcase: ").appendText(e.getMessage());
      return false;
    }
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("an HttpResponse with body ").appendValue(expectedBody);
  }
}
