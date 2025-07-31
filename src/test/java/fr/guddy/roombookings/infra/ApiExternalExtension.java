package fr.guddy.roombookings.infra;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import fr.guddy.roombookings.domain.bookings.Bookings;
import fr.guddy.roombookings.domain.bookings.NitriteBookings;
import fr.guddy.roombookings.domain.rooms.NitriteRooms;
import fr.guddy.roombookings.domain.rooms.Rooms;
import fr.guddy.roombookings.infra.ports.DefaultPort;
import fr.guddy.roombookings.infra.ports.Port;
import org.dizitart.no2.Nitrite;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public final class ApiExternalExtension implements BeforeAllCallback, AfterAllCallback {

    private final Api api;
    private final Rooms rooms;
    private final Bookings bookings;

    private ApiExternalExtension(final Api api, final Rooms rooms, final Bookings bookings) {
        this.api = api;
        this.rooms = rooms;
        this.bookings = bookings;
    }

    public ApiExternalExtension(final Port port, final Rooms rooms, final Bookings bookings) {
        this(
                new Api(port, rooms, bookings),
                rooms,
                bookings
        );
    }

    public ApiExternalExtension(final Port port, final Nitrite database) {
        this(
                port,
                database,
                new NitriteRooms(database)
        );
    }

    public ApiExternalExtension(final Port port, final Nitrite database, final Rooms rooms) {
        this(
                port,
                new NitriteRooms(database),
                new NitriteBookings(database, rooms)
        );
    }

    public ApiExternalExtension() {
        this(
                new DefaultPort(),
                Nitrite.builder().openOrCreate()
        );
    }

    public Rooms rooms() {
        return rooms;
    }

    public Bookings bookings() {
        return bookings;
    }

    @Override
    public void beforeAll(final ExtensionContext context) throws InterruptedException {
        api.start();
        waitForServer();
    }

    @Override
    public void afterAll(final ExtensionContext context) {
        api.stop();
    }

    public void waitForServer() throws InterruptedException {
        int retries = 10;
        int delay = 200; // ms
        boolean isReady = false;

        while (retries-- > 0 && !isReady) {
            try {
                final HttpResponse<String> response = Unirest.get(
                        String.format("http://localhost:%d/ready", api.port().value())
                ).asString();
                if (response.getStatus() == 200 && "READY".equals(response.getBody())) {
                    isReady = true;
                }
            } catch (final UnirestException e) {
                Thread.sleep(delay);
            }
        }

        if (!isReady) {
            throw new RuntimeException("Server didn't start in time");
        }
    }

}
