package fr.guddy.roombookings.infra.requests;

import fr.guddy.roombookings.domain.rooms.RoomNotFoundException;
import io.javalin.Context;

public interface Request {
    void perform(final Context context) throws Exception;
}
