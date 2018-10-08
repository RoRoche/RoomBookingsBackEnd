package fr.guddy.roombookings.domain.rooms;

public final class RoomNotFoundException extends RuntimeException {

    public RoomNotFoundException(final String roomName) {
        super(
                String.format("No room found for name '%s'", roomName)
        );
    }
}
