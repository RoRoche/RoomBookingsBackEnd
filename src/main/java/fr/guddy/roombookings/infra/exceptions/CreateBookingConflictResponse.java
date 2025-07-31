package fr.guddy.roombookings.infra.exceptions;

import fr.guddy.roombookings.domain.bookings.CreateBookingConflictException;
import io.javalin.http.Context;
import io.javalin.http.ExceptionHandler;
import org.eclipse.jetty.http.HttpStatus;

public final class CreateBookingConflictResponse implements ExceptionHandler<CreateBookingConflictException> {
    @Override
    public void handle(final CreateBookingConflictException exception, final Context ctx) {
        ctx.status(HttpStatus.CONFLICT_409).result(exception.getMessage());
    }
}
