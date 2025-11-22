package fr.guddy.roombookings.infra.assertions.requests;

import com.mashape.unirest.http.HttpResponse;
import fr.guddy.roombookings.infra.matchers.HttpResponseBodyMatcher;
import org.hamcrest.MatcherAssert;
import org.hamcrest.text.IsEqualIgnoringCase;

public final class RequestWithBodyAssertion implements RequestAssertion {

  private final RequestAssertion delegate;
  private final String expectedBody;

  public RequestWithBodyAssertion(final RequestAssertion delegate, final String expectedBody) {
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
    MatcherAssert.assertThat(response(), new HttpResponseBodyMatcher(this.expectedBody));
  }
}
