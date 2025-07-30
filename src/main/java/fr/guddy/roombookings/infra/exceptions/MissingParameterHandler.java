package fr.guddy.roombookings.infra.exceptions;

import fr.guddy.roombookings.infra.params.exceptions.MissingParameterException;
import io.javalin.Context;
import io.javalin.ExceptionHandler;
import org.eclipse.jetty.http.HttpStatus;

public final class MissingParameterHandler implements ExceptionHandler<MissingParameterException> {
    @Override
    public void handle(final MissingParameterException exception, final Context ctx) {
        ctx.status(HttpStatus.BAD_REQUEST_400).result(exception.getMessage());
    }
}
