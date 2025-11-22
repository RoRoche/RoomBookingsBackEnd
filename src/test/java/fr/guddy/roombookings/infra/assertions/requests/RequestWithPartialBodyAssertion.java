package fr.guddy.roombookings.infra.assertions.requests;

import static org.hamcrest.Matchers.containsString;

import com.mashape.unirest.http.HttpResponse;
import fr.guddy.roombookings.infra.matchers.HttpResponseBodyContainsMatcher;
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
    return this.delegate.response();
  }

  @Override
  public void check() {
    this.delegate.check();
    MatcherAssert.assertThat(response(), new HttpResponseBodyContainsMatcher(this.expectedBody));
  }
}
