package fr.guddy.roombookings.infra.assertions.requests;

import com.mashape.unirest.http.Headers;
import com.mashape.unirest.http.HttpResponse;

import static org.assertj.core.api.Assertions.assertThat;

public final class RequestWithLocationHeaderAssertion implements RequestAssertion {

    private final RequestAssertion delegate;
    private final String startUri;

    public RequestWithLocationHeaderAssertion(final RequestAssertion delegate, final String startUri) {
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
        assertThat(headers).containsKey("Location");
        assertThat(
                headers.getFirst("Location")
        ).startsWith(startUri);
    }
}
