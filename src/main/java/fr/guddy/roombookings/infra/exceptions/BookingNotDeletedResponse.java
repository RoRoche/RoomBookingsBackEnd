package fr.guddy.roombookings.infra.exceptions;

import fr.guddy.roombookings.domain.bookings.BookingNotDeletedException;
import io.javalin.http.Context;
import io.javalin.http.ExceptionHandler;
import org.eclipse.jetty.http.HttpStatus;

public final class BookingNotDeletedResponse implements ExceptionHandler<BookingNotDeletedException> {
    @Override
    public void handle(final BookingNotDeletedException exception, final Context ctx) {
        ctx.status(HttpStatus.INTERNAL_SERVER_ERROR_500).result(exception.getMessage());
    }
}
