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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import fr.guddy.roombookings.infra.HttpTestCase;
import java.util.Map;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public final class HasBodyContaining extends TypeSafeDiagnosingMatcher<HttpTestCase<String>> {

  private final Map<String, Object> expectedEntries;

  public HasBodyContaining(final Map<String, Object> expectedEntries) {
    this.expectedEntries = expectedEntries;
  }

  @Override
  protected boolean matchesSafely(final HttpTestCase<String> testCase, final Description mismatch) {
    try {
      final ObjectMapper mapper = new ObjectMapper();
      final HttpResponse<String> response = testCase.response();
      final Map<String, Object> actual = mapper.readValue(
        response.getBody(),
        new TypeReference<>() {}
      );
      final Boolean matches = new DeepMatches().apply(actual, expectedEntries);
      if (!matches) {
        mismatch.appendText("body was ").appendValue(response.getBody());
      }
      return matches;
    } catch (final Exception e) {
      mismatch.appendText("exception while executing testcase: ").appendText(e.getMessage());
      return false;
    }
  }

  @Override
  public void describeTo(final Description description) {
    description.appendText("HttpResponse body containing body ").appendValue(this.expectedEntries);
  }
}
