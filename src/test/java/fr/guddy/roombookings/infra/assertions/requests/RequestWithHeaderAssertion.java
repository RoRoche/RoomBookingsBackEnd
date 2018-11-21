package fr.guddy.roombookings.infra.assertions.requests;

import com.mashape.unirest.http.Headers;
import com.mashape.unirest.http.HttpResponse;

import static org.assertj.core.api.Assertions.assertThat;

public final class RequestWithHeaderAssertion implements RequestAssertion {

    private final RequestAssertion delegate;
    private final String key;
    private final String expectedValue;

    public RequestWithHeaderAssertion(final RequestAssertion delegate, final String key, final String expectedValue) {
        this.delegate = delegate;
        this.key = key;
        this.expectedValue = expectedValue;
    }

    @Override
    public HttpResponse<String> response() {
        return delegate.response();
    }

    @Override
    public void check() {
        delegate.check();
        final Headers headers = response().getHeaders();
        assertThat(headers).containsKey(key);
        assertThat(
                headers.getFirst(key)
        ).isEqualToIgnoringCase(expectedValue);
    }
}
