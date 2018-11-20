package fr.guddy.roombookings.infra.assertions;

import com.mashape.unirest.http.HttpResponse;

public final class RequestWithBodyAssertion implements RequestAssertion {

    private final RequestAssertion delegate;

    public RequestWithBodyAssertion(final RequestAssertion<String> delegate) {
        this.delegate = delegate;
    }

    @Override
    public HttpResponse<String> response() {
        return null;
    }

    @Override
    public void check() {

    }
}
