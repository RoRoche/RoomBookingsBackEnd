package fr.guddy.roombookings.domain.room

import java.io.StringReader
import java.util.*
import javax.json.Json
import javax.json.JsonObject

class JsonRoom(private val delegate: Room) : Room by delegate {

    constructor(body: String) : this(
            Json.createReader(
                    StringReader(body)
            ).readObject()
    )

    constructor(jsonObject: JsonObject) : this(
            SimpleRoom(
                    jsonObject.getString(JSON_KEY_NAME),
                    jsonObject.getInt(JSON_KEY_CAPACITY)
            )
    )

    override fun map(): Map<String, Any> {
        val map = LinkedHashMap<String, Any>()
        map[JSON_KEY_NAME] = name()
        map[JSON_KEY_CAPACITY] = capacity()
        return map
    }

    companion object {
        private const val JSON_KEY_NAME = "name"
        private const val JSON_KEY_CAPACITY = "capacity"
    }
}
