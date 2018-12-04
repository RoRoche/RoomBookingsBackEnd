package fr.guddy.roombookings.domain.room

class SimpleRoom(private val name: String, private val capacity: Int) : Room {

    override fun name(): String {
        return name
    }

    override fun capacity(): Int {
        return capacity
    }

    override fun map(): Map<String, Any> = linkedMapOf(
            "name" to name,
            "capacity" to capacity
    )
}
