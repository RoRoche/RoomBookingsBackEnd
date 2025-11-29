package fr.guddy.roombookings.infra.matchers;

import com.mashape.unirest.http.HttpResponse;
import fr.guddy.roombookings.infra.HttpTestCase;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public final class HasStatus extends TypeSafeDiagnosingMatcher<HttpTestCase<?>> {

  private final int expectedStatus;

  public HasStatus(final int expectedStatus) {
    this.expectedStatus = expectedStatus;
  }

  @Override
  protected boolean matchesSafely(final HttpTestCase<?> testCase, final Description mismatch) {
    try {
      final HttpResponse<?> response = testCase.response();
      final boolean matches = response.getStatus() == expectedStatus;
      if (!matches) {
        mismatch.appendText("was HttpResponse with status ").appendValue(response.getStatus());
      }
      return matches;
    } catch (final Exception e) {
      mismatch.appendText("exception while executing testcase: ").appendText(e.getMessage());
      return false;
    }
  }

  @Override
  public void describeTo(final Description description) {
    description.appendText("an HttpResponse with status ").appendValue(expectedStatus);
  }
}
