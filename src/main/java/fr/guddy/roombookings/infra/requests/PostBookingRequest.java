package fr.guddy.roombookings.infra.requests;

import fr.guddy.roombookings.domain.booking.Booking;
import fr.guddy.roombookings.domain.booking.JsonBooking;
import fr.guddy.roombookings.domain.booking.SimpleBooking;
import fr.guddy.roombookings.domain.bookings.BookingNotFoundException;
import fr.guddy.roombookings.domain.bookings.Bookings;
import fr.guddy.roombookings.domain.bookings.CreateBookingConflictException;
import fr.guddy.roombookings.domain.rooms.RoomNotFoundException;
import fr.guddy.roombookings.domain.rooms.Rooms;
import fr.guddy.roombookings.infra.params.Parameter;
import fr.guddy.roombookings.infra.params.PathParameter;
import fr.guddy.roombookings.infra.params.RequiredParameter;
import io.javalin.http.Context;
import org.eclipse.jetty.http.HttpStatus;

public final class PostBookingRequest implements Request {

    private final Bookings bookings;
    private final Booking booking;

    public PostBookingRequest(final Bookings bookings, final Booking booking) {
        this.bookings = bookings;
        this.booking = booking;
    }

    public PostBookingRequest(final Rooms rooms, final Bookings bookings, final Context context) {
        this(
                rooms,
                bookings,
                new RequiredParameter<>(
                        new PathParameter("name", context)
                ),
                new JsonBooking(context.body())
        );
    }

    public PostBookingRequest(final Rooms rooms,
                              final Bookings bookings,
                              final Parameter<String> roomName,
                              final Booking booking) {
        this(
                bookings,
                new SimpleBooking(
                        null,
                        booking.userId(),
                        rooms.withName(roomName.value()).orElseThrow(() ->
                                new RoomNotFoundException(roomName.value())
                        ),
                        booking.slot()
                )
        );
    }

    @Override
    public void perform(final Context context) {
        if (bookings.isConflicting(booking)) {
            throw new CreateBookingConflictException(booking.room().name());
        } else {
            final Long id = bookings.create(booking);
            final Booking actual = bookings.byId(id)
                    .orElseThrow(() -> new BookingNotFoundException(id));
            context.header("location", String.format("/bookings/%d", id))
                    .json(new JsonBooking(actual).map())
                    .status(HttpStatus.CREATED_201);
        }
    }
}
