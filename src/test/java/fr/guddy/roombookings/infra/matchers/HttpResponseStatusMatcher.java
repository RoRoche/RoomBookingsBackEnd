package fr.guddy.roombookings.infra.matchers;

import com.mashape.unirest.http.HttpResponse;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public final class HttpResponseStatusMatcher extends TypeSafeMatcher<HttpResponse<?>> {

  private final int expectedStatus;

  public HttpResponseStatusMatcher(final int expectedStatus) {
    this.expectedStatus = expectedStatus;
  }

  @Override
  protected boolean matchesSafely(final HttpResponse<?> response) {
    return response.getStatus() == expectedStatus;
  }

  @Override
  public void describeTo(final Description description) {
    description.appendText("an HttpResponse with status ").appendValue(expectedStatus);
  }

  @Override
  protected void describeMismatchSafely(
    final HttpResponse<?> response,
    final Description mismatchDescription
  ) {
    mismatchDescription
      .appendText("was HttpResponse with status ")
      .appendValue(response.getStatus());
  }
}
