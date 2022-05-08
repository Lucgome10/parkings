package sanviator.tfg.parkings.utils

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component


@Component
class MqttUtils {


    @Value("\${app.mqttUser}")
    lateinit var mqttUser: String


    @Value("\${app.mqttPassword}")
    lateinit var mqttPassword: String


}