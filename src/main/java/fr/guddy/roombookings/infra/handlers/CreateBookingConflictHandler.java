package fr.guddy.roombookings.infra.handlers;

import fr.guddy.roombookings.domain.bookings.CreateBookingConflictException;
import io.javalin.Context;
import io.javalin.ExceptionHandler;
import org.eclipse.jetty.http.HttpStatus;

public final class CreateBookingConflictHandler implements ExceptionHandler<CreateBookingConflictException> {
    @Override
    public void handle(final CreateBookingConflictException exception, final Context ctx) {
        ctx.status(HttpStatus.CONFLICT_409).result(exception.getMessage());
    }
}
