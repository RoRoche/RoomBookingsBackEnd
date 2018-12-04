package fr.guddy.roombookings;

import fr.guddy.roombookings.domain.bookings.NitriteBookings;
import fr.guddy.roombookings.domain.rooms.NitriteRooms;
import fr.guddy.roombookings.domain.rooms.Rooms;
import fr.guddy.roombookings.infra.Api;
import fr.guddy.roombookings.infra.ports.DefaultPort;
import fr.guddy.roombookings.infra.ports.HerokuAssignedPort;
import org.dizitart.no2.Nitrite;

public final class Main {
    public static void main(final String[] args) {
        final Nitrite database = Nitrite.builder().openOrCreate();
        final Rooms rooms = new NitriteRooms(database);
        new Api(
                new HerokuAssignedPort(new DefaultPort()),
                rooms,
                new NitriteBookings(database, rooms)
        ).start();
    }
}
