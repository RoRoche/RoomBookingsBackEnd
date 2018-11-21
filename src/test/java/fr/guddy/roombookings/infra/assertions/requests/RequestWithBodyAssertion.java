package fr.guddy.roombookings.infra.assertions.requests;

import com.mashape.unirest.http.HttpResponse;

import static org.assertj.core.api.Assertions.assertThat;

public final class RequestWithBodyAssertion implements RequestAssertion {

    private final RequestAssertion delegate;
    private final String expectedBody;

    public RequestWithBodyAssertion(final RequestAssertion delegate, final String expectedBody) {
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
        assertThat(
                response().getBody()
        ).isEqualToIgnoringCase(expectedBody);
    }
}
