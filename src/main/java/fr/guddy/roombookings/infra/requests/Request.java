package fr.guddy.roombookings.infra.requests;

public interface Request {
    void perform(final io.javalin.http.Context context);
}
