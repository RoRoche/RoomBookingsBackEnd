package fr.guddy.roombookings.infra.assertions.requests;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.request.HttpRequest;
import io.vavr.control.Try;

import static org.assertj.core.api.Assertions.assertThat;

public final class RequestHasStatusCodeAssertion implements RequestAssertion {
    private final HttpRequest request;
    private final int expectedStatusCode;

    public RequestHasStatusCodeAssertion(final HttpRequest request, final int expectedStatusCode) {
        this.request = request;
        this.expectedStatusCode = expectedStatusCode;
    }

    @Override
    public HttpResponse<String> response() {
        return Try.of(request::asString).get();
    }

    @Override
    public void check() {
        assertThat(response().getStatus()).isEqualTo(expectedStatusCode);
    }
}
