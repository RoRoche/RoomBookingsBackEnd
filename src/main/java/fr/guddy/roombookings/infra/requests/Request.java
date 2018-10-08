package fr.guddy.roombookings.infra.requests;

import io.javalin.Context;

public interface Request {
    void perform(final Context context);
}
