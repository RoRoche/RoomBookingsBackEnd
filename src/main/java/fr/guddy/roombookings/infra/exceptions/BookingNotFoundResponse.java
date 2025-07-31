package fr.guddy.roombookings.infra.exceptions;

import fr.guddy.roombookings.domain.bookings.BookingNotFoundException;
import io.javalin.http.Context;
import io.javalin.http.ExceptionHandler;
import org.eclipse.jetty.http.HttpStatus;

public final class BookingNotFoundResponse implements ExceptionHandler<BookingNotFoundException> {
    @Override
    public void handle(final BookingNotFoundException exception, final Context ctx) {
        ctx.status(HttpStatus.NOT_FOUND_404).result(exception.getMessage());
    }
}
