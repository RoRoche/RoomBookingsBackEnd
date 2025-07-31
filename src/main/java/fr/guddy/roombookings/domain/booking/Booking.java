package fr.guddy.roombookings.domain.booking;

import fr.guddy.roombookings.domain.room.Room;
import fr.guddy.roombookings.domain.slot.Slot;

import java.util.Map;

public interface Booking {
    Long id();

    String userId();

    Room room();

    Slot slot();

    Map<String, Object> map();

    abstract class Envelope implements Booking {
        private final Booking delegate;

        protected Envelope(final Booking delegate) {
            this.delegate = delegate;
        }

        @Override
        public Long id() {
            return this.delegate.id();
        }

        @Override
        public String userId() {
            return this.delegate.userId();
        }

        @Override
        public Room room() {
            return this.delegate.room();
        }

        @Override
        public Slot slot() {
            return this.delegate.slot();
        }

        @Override
        public Map<String, Object> map() {
            return this.delegate.map();
        }
    }
}
