package fr.guddy.roombookings.domain.room

import org.dizitart.no2.Document

import java.util.LinkedHashMap

class NitriteRoom(private val delegate: Room) : Room {

    constructor(document: Document) : this(
            SimpleRoom(
                    document.get<String>(DOCUMENT_KEY_NAME, String::class.java),
                    document.get<Int>(DOCUMENT_KEY_CAPACITY, Int::class.java)
            )
    )

    override fun name(): String {
        return delegate.name()
    }

    override fun capacity(): Int {
        return delegate.capacity()
    }

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
