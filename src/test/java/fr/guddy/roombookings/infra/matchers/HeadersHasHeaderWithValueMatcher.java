package fr.guddy.roombookings.infra.matchers;

import com.mashape.unirest.http.Headers;
import com.mashape.unirest.http.HttpResponse;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public final class HeadersHasHeaderWithValueMatcher extends TypeSafeMatcher<HttpResponse<?>> {

  private final String headerName;
  private final Matcher<? super String> valueMatcher;

  public HeadersHasHeaderWithValueMatcher(
    final String headerName,
    final Matcher<? super String> valueMatcher
  ) {
    this.headerName = headerName;
    this.valueMatcher = valueMatcher;
  }

  @Override
  protected boolean matchesSafely(final HttpResponse<?> response) {
    final Headers headers = response.getHeaders();
    if (!headers.containsKey(headerName)) {
      return false;
    }
    final String value = headers.getFirst(headerName);
    return valueMatcher.matches(value);
  }

  @Override
  public void describeTo(final Description description) {
    description
      .appendText("HttpResponse with header ")
      .appendValue(headerName)
      .appendText(" having value ")
      .appendDescriptionOf(valueMatcher);
  }

  @Override
  protected void describeMismatchSafely(
    final HttpResponse<?> response,
    final Description mismatchDescription
  ) {
    final Headers headers = response.getHeaders();
    if (!headers.containsKey(headerName)) {
      mismatchDescription.appendText("header keys were ").appendValue(headers.keySet());
    } else {
      mismatchDescription
        .appendText("header ")
        .appendValue(headerName)
        .appendText(" value was ")
        .appendValue(headers.getFirst(headerName));
    }
  }
}
