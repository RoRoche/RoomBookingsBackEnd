package fr.guddy.roombookings.infra.requests;

import fr.guddy.roombookings.domain.booking.Booking;
import fr.guddy.roombookings.domain.booking.JsonBooking;
import fr.guddy.roombookings.domain.booking.SimpleBooking;
import fr.guddy.roombookings.domain.bookings.Bookings;
import fr.guddy.roombookings.domain.bookings.CreateBookingConflictException;
import fr.guddy.roombookings.domain.rooms.RoomNotFoundException;
import fr.guddy.roombookings.domain.rooms.Rooms;
import fr.guddy.roombookings.infra.params.Parameter;
import fr.guddy.roombookings.infra.params.PathParameter;
import fr.guddy.roombookings.infra.params.RequiredParameter;
import io.javalin.Context;
import org.dizitart.no2.WriteResult;
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
                        rooms.namedRoom(roomName.value()).orElseThrow(() ->
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
            final WriteResult result = bookings.create(booking);
            final Long id = result.iterator().next().getIdValue();
            context.header("location", String.format("/bookings/%d", id))
                    .status(HttpStatus.CREATED_201);
        }
    }
}
