package fr.guddy.roombookings.infra.requests;

import io.javalin.http.Context;

public interface Request {
    void perform(final Context context);
}
