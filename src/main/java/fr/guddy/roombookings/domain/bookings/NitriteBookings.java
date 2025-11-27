package fr.guddy.roombookings.domain.bookings;

import static org.dizitart.no2.filters.Filter.and;
import static org.dizitart.no2.filters.Filter.or;
import static org.dizitart.no2.filters.FluentFilter.where;

import fr.guddy.roombookings.domain.booking.Booking;
import fr.guddy.roombookings.domain.booking.NitriteBooking;
import fr.guddy.roombookings.domain.room.Room;
import fr.guddy.roombookings.domain.rooms.Rooms;
import fr.guddy.roombookings.domain.slot.Slot;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.cactoos.Scalar;
import org.cactoos.scalar.Unchecked;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.collection.Document;
import org.dizitart.no2.collection.NitriteCollection;
import org.dizitart.no2.collection.NitriteId;
import org.dizitart.no2.filters.Filter;

public final class NitriteBookings implements Bookings {

  private static final String DOCUMENT_KEY_ROOM_NAME = "room_name";
  private static final String DOCUMENT_KEY_USER_ID = "user_id";
  private static final String DOCUMENT_KEY_SLOT_TIMESTAMP_START = "slot_timestamp_start";
  private static final String DOCUMENT_KEY_SLOT_TIMESTAMP_END = "slot_timestamp_end";

  private final NitriteCollection collection;
  private final Rooms rooms;

  public NitriteBookings(final NitriteCollection collection, final Rooms rooms) {
    this.collection = collection;
    this.rooms = rooms;
  }

  public NitriteBookings(final Nitrite database, final Rooms rooms) {
    this(database.getCollection("bookings"), rooms);
  }

  @Override
  public Long create(final Booking booking) {
    return this.collection.insert(Document.createDocument(new NitriteBooking(booking).map()))
      .iterator()
      .next()
      .getIdValue();
  }

  @Override
  public List<Booking> forRoomInSlot(final Room room, final Slot slot) {
    final List<Document> documents = this.collection.find(
      and(
        where(DOCUMENT_KEY_ROOM_NAME).eq(room.name()),
        or(
          and(
            where(DOCUMENT_KEY_SLOT_TIMESTAMP_START).lte(slot.timestampEnd()),
            where(DOCUMENT_KEY_SLOT_TIMESTAMP_END).gte(slot.timestampEnd())
          ),
          and(
            where(DOCUMENT_KEY_SLOT_TIMESTAMP_START).lte(slot.timestampStart()),
            where(DOCUMENT_KEY_SLOT_TIMESTAMP_END).gte(slot.timestampStart())
          ),
          and(
            where(DOCUMENT_KEY_SLOT_TIMESTAMP_START).gte(slot.timestampStart()),
            where(DOCUMENT_KEY_SLOT_TIMESTAMP_END).lte(slot.timestampEnd())
          )
        )
      )
    ).toList();
    return documents
      .stream()
      .map((document) ->
        new Unchecked<>((Scalar<Booking>) () -> new NitriteBooking(document, rooms)).value()
      )
      .toList();
  }

  @Override
  public List<Booking> forUserFromStartDate(final String userId, final long timestampStart) {
    final List<Document> documents = this.collection.find(
      and(
        where(DOCUMENT_KEY_USER_ID).eq(userId),
        or(
          where(DOCUMENT_KEY_SLOT_TIMESTAMP_START).gte(timestampStart),
          where(DOCUMENT_KEY_SLOT_TIMESTAMP_END).gte(timestampStart)
        )
      )
    ).toList();
    return documents
      .stream()
      .map((document) ->
        new Unchecked<>((Scalar<Booking>) () -> new NitriteBooking(document, rooms)).value()
      )
      .sorted(Comparator.comparingLong((booking) -> booking.slot().timestampStart()))
      .toList();
  }

  @Override
  public boolean isConflicting(final Booking booking) {
    return !forRoomInSlot(booking.room(), booking.slot()).isEmpty();
  }

  @Override
  public int clearAll() {
    return this.collection.remove(Filter.ALL).getAffectedCount();
  }

  @Override
  public Optional<Booking> byId(final long id) {
    return Optional.ofNullable(this.collection.getById(NitriteId.createId(id))).map((document) ->
      new NitriteBooking(document, this.rooms)
    );
  }

  @Override
  public boolean delete(final Booking booking) {
    return (
      this.collection.remove(
        this.collection.getById(NitriteId.createId(booking.id()))
      ).getAffectedCount() ==
      1
    );
  }
}
