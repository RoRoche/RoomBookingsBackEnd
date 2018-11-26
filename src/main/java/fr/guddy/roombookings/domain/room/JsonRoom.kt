package fr.guddy.roombookings.domain.room

import javax.json.Json
import javax.json.JsonObject
import java.io.StringReader
import java.util.LinkedHashMap

class JsonRoom(private val delegate: Room) : Room {

    constructor(body: String) : this(
            Json.createReader(
                    StringReader(body)
            ).readObject()
    ) {
    }

    constructor(jsonObject: JsonObject) : this(
            SimpleRoom(
                    jsonObject.getString(JSON_KEY_NAME),
                    jsonObject.getInt(JSON_KEY_CAPACITY)
            )
    ) {
    }

    override fun name(): String {
        return delegate.name()
    }

    override fun capacity(): Int {
        return delegate.capacity()
    }

    override fun map(): Map<String, Any> {
        val map = LinkedHashMap<String, Any>()
        map[JSON_KEY_NAME] = name()
        map[JSON_KEY_CAPACITY] = capacity()
        return map
    }

    companion object {
        private val JSON_KEY_NAME = "name"
        private val JSON_KEY_CAPACITY = "capacity"
    }
}
