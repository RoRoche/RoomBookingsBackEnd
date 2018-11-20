package fr.guddy.roombookings.domain.rooms;

public final class CreateRoomConflictException extends RuntimeException {
    public CreateRoomConflictException(final Throwable throwable, final String roomName) {
        super(
                String.format("A room named '%s' already exists", roomName),
                throwable
        );
    }
}
