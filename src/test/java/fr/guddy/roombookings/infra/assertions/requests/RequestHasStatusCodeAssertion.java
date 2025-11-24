package fr.guddy.roombookings.infra.assertions.requests;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.request.HttpRequest;
import fr.guddy.roombookings.infra.matchers.HttpResponseStatusMatcher;
import io.vavr.Lazy;
import io.vavr.control.Try;
import org.hamcrest.MatcherAssert;

public final class RequestHasStatusCodeAssertion implements RequestAssertion {

  private final HttpRequest request;
  private final int expectedStatusCode;
  private final Lazy<HttpResponse<String>> response;

  public RequestHasStatusCodeAssertion(final HttpRequest request, final int expectedStatusCode) {
    this.request = request;
    this.expectedStatusCode = expectedStatusCode;
    this.response = Lazy.of(this::performRequest);
  }

  private HttpResponse<String> performRequest() {
    return Try.of(this.request::asString).get();
  }

  @Override
  public HttpResponse<String> response() {
    return this.response.get();
  }

  @Override
  public void check() {
    MatcherAssert.assertThat(response(), new HttpResponseStatusMatcher(this.expectedStatusCode));
  }
}
