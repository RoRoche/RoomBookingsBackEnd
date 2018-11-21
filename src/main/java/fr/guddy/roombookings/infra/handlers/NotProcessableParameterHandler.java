package fr.guddy.roombookings.infra.handlers;

import fr.guddy.roombookings.infra.params.exceptions.NotProcessableParameterException;
import io.javalin.Context;
import io.javalin.ExceptionHandler;
import org.eclipse.jetty.http.HttpStatus;

public final class NotProcessableParameterHandler implements ExceptionHandler<NotProcessableParameterException> {
    @Override
    public void handle(final NotProcessableParameterException exception, final Context ctx) {
        ctx.status(HttpStatus.BAD_REQUEST_400).result(exception.getMessage());
    }
}
