package sanviator.tfg.parkings.enums

enum class MqttEnum(val value: String) {

    BROKER ("tcp://localhost:1991"),
    TOPIC_SLOTS ("slots");

    companion object {
        private val map = values().associateBy(MqttEnum::value)
        fun fromString(configuration: String) = map[configuration]
    }
}