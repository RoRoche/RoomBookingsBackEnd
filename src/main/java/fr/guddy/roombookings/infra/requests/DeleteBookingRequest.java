package fr.guddy.roombookings.infra.requests;

import fr.guddy.roombookings.domain.bookings.BookingNotDeletedException;
import fr.guddy.roombookings.domain.bookings.BookingNotFoundException;
import fr.guddy.roombookings.domain.bookings.Bookings;
import fr.guddy.roombookings.infra.params.LongParameter;
import fr.guddy.roombookings.infra.params.PathParameter;
import fr.guddy.roombookings.infra.params.RequiredParameter;
import io.javalin.Context;
import org.dizitart.no2.Document;
import org.dizitart.no2.WriteResult;
import org.eclipse.jetty.http.HttpStatus;

public final class DeleteBookingRequest implements Request {

    private final Bookings bookings;
    private final long id;

    public DeleteBookingRequest(final Bookings bookings, final long id) {
        this.bookings = bookings;
        this.id = id;
    }

    public DeleteBookingRequest(final Bookings bookings, final Context context) {
        this(
                bookings,
                new LongParameter(
                        new RequiredParameter<>(
                                new PathParameter("id", context)
                        )
                ).value()
        );
    }

    @Override
    public void perform(final Context context) {
        final Document document = bookings.documentById(id)
                .orElseThrow(() -> new BookingNotFoundException(id));
        final WriteResult result = bookings.delete(document);
        if (result.getAffectedCount() >= 1) {
            context.status(HttpStatus.OK_200);
        } else {
            throw new BookingNotDeletedException(id);
        }
    }
}
