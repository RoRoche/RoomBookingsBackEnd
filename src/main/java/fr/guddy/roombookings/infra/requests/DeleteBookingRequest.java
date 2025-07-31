package fr.guddy.roombookings.infra.requests;

import fr.guddy.roombookings.domain.booking.Booking;
import fr.guddy.roombookings.domain.booking.JsonBooking;
import fr.guddy.roombookings.domain.bookings.BookingNotDeletedException;
import fr.guddy.roombookings.domain.bookings.BookingNotFoundException;
import fr.guddy.roombookings.domain.bookings.Bookings;
import fr.guddy.roombookings.domain.rooms.Rooms;
import fr.guddy.roombookings.infra.params.LongParameter;
import fr.guddy.roombookings.infra.params.PathParameter;
import fr.guddy.roombookings.infra.params.RequiredParameter;
import io.javalin.http.Context;
import org.eclipse.jetty.http.HttpStatus;

public final class DeleteBookingRequest implements Request {

    private final Rooms rooms;
    private final Bookings bookings;
    private final long id;

    public DeleteBookingRequest(final Rooms rooms, final Bookings bookings, final long id) {
        this.rooms = rooms;
        this.bookings = bookings;
        this.id = id;
    }

    public DeleteBookingRequest(final Rooms rooms, final Bookings bookings, final Context context) {
        this(
                rooms,
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
        final Booking booking = bookings.bookingById(id)
                .orElseThrow(() -> new BookingNotFoundException(id));
        final boolean result = bookings.delete(booking);
        if (result) {
            context.json(new JsonBooking(booking).map()).status(HttpStatus.OK_200);
        } else {
            throw new BookingNotDeletedException(id);
        }
    }
}
