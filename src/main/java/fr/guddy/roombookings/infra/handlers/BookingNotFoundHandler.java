package fr.guddy.roombookings.infra.handlers;

import fr.guddy.roombookings.domain.bookings.BookingNotFoundException;
import io.javalin.Context;
import io.javalin.ExceptionHandler;
import org.eclipse.jetty.http.HttpStatus;

public final class BookingNotFoundHandler implements ExceptionHandler<BookingNotFoundException> {
    @Override
    public void handle(final BookingNotFoundException exception, final Context ctx) {
        ctx.status(HttpStatus.NOT_FOUND_404).result(exception.getMessage());
    }
}
