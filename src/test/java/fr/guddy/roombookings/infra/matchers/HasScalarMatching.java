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

import fr.guddy.roombookings.infra.HttpTestCase;
import fr.guddy.roombookings.infra.HttpTestCaseEnvelope;
import org.cactoos.Func;
import org.cactoos.Scalar;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public final class HasScalarMatching<T extends HttpTestCaseEnvelope>
  extends TypeSafeDiagnosingMatcher<Scalar<T>> {

  private final Func<T, Matcher<HttpTestCase<String>>> matcherFunc;

  public HasScalarMatching(final Func<T, Matcher<HttpTestCase<String>>> matcherFunc) {
    this.matcherFunc = matcherFunc;
  }

  @Override
  protected boolean matchesSafely(final Scalar<T> result, final Description mismatch) {
    try {
      final T envelope = result.value();
      final HttpTestCase<String> testCase = envelope.value();
      final Matcher<HttpTestCase<String>> matcher = this.matcherFunc.apply(envelope);
      final boolean matches = matcher.matches(testCase);
      if (!matches) {
        matcher.describeMismatch(testCase, mismatch);
      }
      return matches;
    } catch (final Exception e) {
      mismatch.appendText("exception while executing testcase: ").appendText(e.getMessage());
      return false;
    }
  }

  @Override
  public void describeTo(final Description description) {
    description.appendText("test case must match");
  }
}
