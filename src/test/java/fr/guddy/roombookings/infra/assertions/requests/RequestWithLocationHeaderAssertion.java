package fr.guddy.roombookings.infra.assertions.requests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.startsWith;

import com.mashape.unirest.http.HttpResponse;
import fr.guddy.roombookings.infra.matchers.HeadersHasHeaderWithValueMatcher;

public final class RequestWithLocationHeaderAssertion implements RequestAssertion {

  private final RequestAssertion delegate;
  private final String startUri;

  public RequestWithLocationHeaderAssertion(
    final RequestAssertion delegate,
    final String startUri
  ) {
    this.delegate = delegate;
    this.startUri = startUri;
  }

  @Override
  public HttpResponse<String> response() {
    return this.delegate.response();
  }

  @Override
  public void check() {
    this.delegate.check();
    assertThat(
      response(),
      new HeadersHasHeaderWithValueMatcher("Location", startsWith(this.startUri))
    );
  }
}
