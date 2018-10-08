package fr.guddy.roombookings.infra.requests;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import fr.guddy.roombookings.domain.room.SimpleRoom;
import fr.guddy.roombookings.domain.rooms.NitriteRooms;
import fr.guddy.roombookings.domain.rooms.Rooms;
import fr.guddy.roombookings.infra.ApiExternalResource;
import org.dizitart.no2.Nitrite;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Rule;
import org.junit.Test;

import static com.mashape.unirest.http.Unirest.get;
import static org.assertj.core.api.Assertions.assertThat;

public final class GetRoomsRequestTest {
    private final Nitrite database = Nitrite.builder().openOrCreate();
    private final Rooms rooms = new NitriteRooms(database);
    @Rule
    public final ApiExternalResource apiExternalResource = new ApiExternalResource(
            7000,
            rooms
    );

    @Test
    public void testNoContent() throws UnirestException {
        // given
        final GetRequest request = get("http://localhost:7000/rooms");

        // when
        final HttpResponse<String> response = request.asString();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT_204);
    }

    @Test
    public void testOK() throws UnirestException {
        // given
        rooms.create(new SimpleRoom("test_name", 12));
        final GetRequest request = get("http://localhost:7000/rooms");

        // when
        final HttpResponse<String> response = request.asString();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK_200);
        assertThat(
                response.getBody()
        ).isEqualToIgnoringCase("[{\"name\":\"test_name\",\"capacity\":12}]");
    }
}