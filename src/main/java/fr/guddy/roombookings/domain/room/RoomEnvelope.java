package fr.guddy.roombookings.domain.room;

import java.util.Map;

public abstract class RoomEnvelope implements Room {

  private final Room delegate;

  protected RoomEnvelope(final Room delegate) {
    this.delegate = delegate;
  }

  @Override
  public String name() {
    return this.delegate.name();
  }

  @Override
  public int capacity() {
    return this.delegate.capacity();
  }

  @Override
  public Map<String, Object> map() {
    return this.delegate.map();
  }
}
