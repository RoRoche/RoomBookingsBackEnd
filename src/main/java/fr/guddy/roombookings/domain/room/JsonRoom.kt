package fr.guddy.roombookings.domain.room

import java.io.StringReader
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

    override fun map(): Map<String, Any> = linkedMapOf(
            JSON_KEY_NAME to name(),
            JSON_KEY_CAPACITY to capacity()
    )

    companion object {
        private const val JSON_KEY_NAME = "name"
        private const val JSON_KEY_CAPACITY = "capacity"
    }
}
