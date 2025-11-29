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
