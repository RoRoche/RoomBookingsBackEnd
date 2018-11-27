package fr.guddy.roombookings.domain.room

import org.dizitart.no2.Document

import java.util.LinkedHashMap

class NitriteRoom(private val delegate: Room) : Room by delegate {

    constructor(document: Document) : this(
            SimpleRoom(
                    document.get<String>(DOCUMENT_KEY_NAME, String::class.java),
                    document.get<Integer>(DOCUMENT_KEY_CAPACITY, Integer::class.java).toInt()
            )
    )

    override fun map(): Map<String, Any> {
        val map = LinkedHashMap<String, Any>()
        map[DOCUMENT_KEY_NAME] = name()
        map[DOCUMENT_KEY_CAPACITY] = capacity()
        return map
    }

    companion object {
        private const val DOCUMENT_KEY_NAME = "room_name"
        private const val DOCUMENT_KEY_CAPACITY = "room_capacity"
    }
}
