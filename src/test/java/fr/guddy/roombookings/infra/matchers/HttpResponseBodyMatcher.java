package fr.guddy.roombookings.infra.matchers;

import com.mashape.unirest.http.HttpResponse;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public final class HttpResponseBodyMatcher extends TypeSafeMatcher<HttpResponse<String>> {

  private final String expectedBody;

  public HttpResponseBodyMatcher(final String expectedBody) {
    this.expectedBody = expectedBody;
  }

  @Override
  protected boolean matchesSafely(final HttpResponse<String> response) {
    return expectedBody.equalsIgnoreCase(response.getBody());
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("an HttpResponse with body ").appendValue(expectedBody);
  }

  @Override
  protected void describeMismatchSafely(
    HttpResponse<String> response,
    Description mismatchDescription
  ) {
    mismatchDescription.appendText("was HttpResponse with body ").appendValue(response.getBody());
  }
}
