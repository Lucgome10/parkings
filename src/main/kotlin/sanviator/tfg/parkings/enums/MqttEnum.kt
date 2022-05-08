package sanviator.tfg.parkings.enums

enum class MqttEnum(val value: String) {

    BROKER ("tcp://207.154.239.11:1991"),
    TOPIC_SLOTS ("slots");

    companion object {
        private val map = values().associateBy(MqttEnum::value)
        fun fromString(configuration: String) = map[configuration]
    }
}