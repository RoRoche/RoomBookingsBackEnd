package fr.guddy.roombookings.infra.matchers;

import com.mashape.unirest.http.HttpResponse;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public final class HasBodyContaining extends TypeSafeMatcher<HttpResponse<String>> {

  private final String expectedSubstring;

  public HasBodyContaining(final String expectedSubstring) {
    this.expectedSubstring = expectedSubstring.toLowerCase(); // insensible à la casse
  }

  @Override
  protected boolean matchesSafely(final HttpResponse<String> response) {
    final String body = response.getBody();
    return body != null && body.toLowerCase().contains(expectedSubstring.toLowerCase());
  }

  @Override
  public void describeTo(final Description description) {
    description
      .appendText("HttpResponse body containing (case-insensitive) ")
      .appendValue(expectedSubstring);
  }

  @Override
  protected void describeMismatchSafely(
    final HttpResponse<String> response,
    final Description mismatchDescription
  ) {
    mismatchDescription.appendText("body was ").appendValue(response.getBody());
  }
}
