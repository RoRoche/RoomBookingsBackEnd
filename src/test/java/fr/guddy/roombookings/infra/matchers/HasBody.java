package fr.guddy.roombookings.infra.matchers;

import com.mashape.unirest.http.HttpResponse;
import fr.guddy.roombookings.infra.HttpTestCase;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public final class HasBody extends TypeSafeDiagnosingMatcher<HttpTestCase<String>> {

  private final String expectedBody;

  public HasBody(final String expectedBody) {
    this.expectedBody = expectedBody;
  }

  @Override
  protected boolean matchesSafely(final HttpTestCase<String> testCase, final Description mismatch) {
    try {
      final HttpResponse<String> response = testCase.response();
      final boolean matches = expectedBody.equalsIgnoreCase(response.getBody());
      if (!matches) {
        mismatch.appendText("was HttpResponse with body ").appendValue(response.getBody());
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
