package fr.guddy.roombookings.domain.rooms;

import java.util.Map;

public interface Room {
    String name();

    int capacity();

    Map<String, Object> map();
}
