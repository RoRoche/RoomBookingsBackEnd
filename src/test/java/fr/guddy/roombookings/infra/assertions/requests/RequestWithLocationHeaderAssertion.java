package fr.guddy.roombookings.infra.assertions.requests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasKey;

import com.mashape.unirest.http.Headers;
import com.mashape.unirest.http.HttpResponse;
import org.hamcrest.core.StringStartsWith;

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
    return delegate.response();
  }

  @Override
  public void check() {
    delegate.check();
    final Headers headers = response().getHeaders();
    assertThat(headers, hasKey("Location"));
    assertThat(headers.getFirst("Location"), new StringStartsWith(startUri));
  }
}
