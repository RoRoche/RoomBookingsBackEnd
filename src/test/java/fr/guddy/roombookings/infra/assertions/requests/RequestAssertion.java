package fr.guddy.roombookings.infra.assertions;

import com.mashape.unirest.http.HttpResponse;

public interface RequestAssertion extends Assertion {
    HttpResponse<String> response();
}
