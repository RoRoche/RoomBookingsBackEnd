package fr.guddy.roombookings.infra.assertions.requests;

import static org.hamcrest.Matchers.containsString;

import com.mashape.unirest.http.HttpResponse;
import org.hamcrest.MatcherAssert;

public final class RequestWithPartialBodyAssertion implements RequestAssertion {

  private final RequestAssertion delegate;
  private final String expectedBody;

  public RequestWithPartialBodyAssertion(
    final RequestAssertion delegate,
    final String expectedBody
  ) {
    this.delegate = delegate;
    this.expectedBody = expectedBody;
  }

  @Override
  public HttpResponse<String> response() {
    return delegate.response();
  }

  @Override
  public void check() {
    delegate.check();
    String actual = response().getBody().toLowerCase();
    String expected = expectedBody.toLowerCase();

    MatcherAssert.assertThat(actual, containsString(expected));
  }
}
