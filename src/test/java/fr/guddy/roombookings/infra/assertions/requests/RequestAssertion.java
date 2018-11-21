package fr.guddy.roombookings.infra.assertions.requests;

import com.mashape.unirest.http.HttpResponse;
import fr.guddy.roombookings.assertions.Assertion;

public interface RequestAssertion extends Assertion {
    HttpResponse<String> response();
}
