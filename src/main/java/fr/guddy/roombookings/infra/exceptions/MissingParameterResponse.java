package fr.guddy.roombookings.infra.exceptions;

import fr.guddy.roombookings.infra.params.exceptions.MissingParameterException;
import io.javalin.http.Context;
import io.javalin.http.ExceptionHandler;
import org.eclipse.jetty.http.HttpStatus;

public final class MissingParameterResponse implements ExceptionHandler<MissingParameterException> {
    @Override
    public void handle(final MissingParameterException exception, final Context ctx) {
        ctx.status(HttpStatus.BAD_REQUEST_400).result(exception.getMessage());
    }
}
