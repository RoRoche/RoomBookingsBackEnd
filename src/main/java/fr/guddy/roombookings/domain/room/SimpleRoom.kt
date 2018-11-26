package fr.guddy.roombookings.domain.room

import java.util.LinkedHashMap

class SimpleRoom(private val name: String, private val capacity: Int) : Room {

    override fun name(): String {
        return name
    }

    override fun capacity(): Int {
        return capacity
    }

    override fun map(): Map<String, Any> {
        val map = LinkedHashMap<String, Any>()
        map["name"] = name
        map["capacity"] = capacity
        return map
    }
}
