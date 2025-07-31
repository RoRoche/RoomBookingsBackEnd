package fr.guddy.roombookings.infra.exceptions;

import fr.guddy.roombookings.domain.rooms.CreateRoomConflictException;
import io.javalin.http.Context;
import io.javalin.http.ExceptionHandler;
import org.eclipse.jetty.http.HttpStatus;

public final class CreateRoomConflictResponse implements ExceptionHandler<CreateRoomConflictException> {
    @Override
    public void handle(final CreateRoomConflictException exception, final Context ctx) {
        ctx.status(HttpStatus.CONFLICT_409).result(exception.getMessage());
    }
}
