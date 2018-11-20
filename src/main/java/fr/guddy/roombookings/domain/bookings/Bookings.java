package fr.guddy.roombookings.domain.bookings;

import fr.guddy.roombookings.domain.booking.Booking;
import fr.guddy.roombookings.domain.room.Room;
import fr.guddy.roombookings.domain.slot.Slot;
import org.dizitart.no2.Document;
import org.dizitart.no2.WriteResult;

import java.util.List;
import java.util.Optional;

public interface Bookings {
    WriteResult create(final Booking booking);

    List<Booking> bookingsForRoomInSlot(final Room room, final Slot slot);

    List<Booking> bookingsForUserFromStartDate(final String userId, final long timestampStart);

    boolean isConflicting(final Booking booking);

    WriteResult clearAll();

    Optional<Document> documentById(final long id);

    WriteResult delete(final Document document);
}
