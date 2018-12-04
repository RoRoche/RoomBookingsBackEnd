package fr.guddy.roombookings.infra;

import fr.guddy.roombookings.domain.bookings.Bookings;
import fr.guddy.roombookings.domain.bookings.NitriteBookings;
import fr.guddy.roombookings.domain.rooms.NitriteRooms;
import fr.guddy.roombookings.domain.rooms.Rooms;
import fr.guddy.roombookings.infra.ports.DefaultPort;
import fr.guddy.roombookings.infra.ports.Port;
import org.dizitart.no2.Nitrite;
import org.junit.rules.ExternalResource;

public final class ApiExternalResource extends ExternalResource {

    private final Api api;
    private final Rooms rooms;
    private final Bookings bookings;

    private ApiExternalResource(final Api api, final Rooms rooms, final Bookings bookings) {
        this.api = api;
        this.rooms = rooms;
        this.bookings = bookings;
    }

    public ApiExternalResource(final Port port, final Rooms rooms, final Bookings bookings) {
        this(
                new Api(port, rooms, bookings),
                rooms,
                bookings
        );
    }

    public ApiExternalResource(final Port port, final Nitrite database) {
        this(
                port,
                database,
                new NitriteRooms(database)
        );
    }

    public ApiExternalResource(final Port port, final Nitrite database, final Rooms rooms) {
        this(
                port,
                new NitriteRooms(database),
                new NitriteBookings(database, rooms)
        );
    }

    public ApiExternalResource() {
        this(
                new DefaultPort(),
                Nitrite.builder().openOrCreate()
        );
    }

    @Override
    protected void before() throws Throwable {
        super.before();
        api.start();
    }

    @Override
    protected void after() {
        super.after();
        api.stop();
    }

    public Rooms rooms() {
        return rooms;
    }

    public Bookings bookings() {
        return bookings;
    }
}
