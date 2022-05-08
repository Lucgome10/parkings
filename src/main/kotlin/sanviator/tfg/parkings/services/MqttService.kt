package sanviator.tfg.parkings.services

import com.google.gson.Gson
import org.eclipse.paho.client.mqttv3.MqttAsyncClient
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import sanviator.tfg.parkings.enums.MqttEnum
import sanviator.tfg.parkings.model.SlotsDTO
import sanviator.tfg.parkings.utils.MqttUtils
import javax.annotation.PostConstruct


@Service
class MqttService() {

    @Autowired
    lateinit var mqttUtils: MqttUtils


    val slotsTopic = MqttEnum.TOPIC_SLOTS.value
    final val broker = MqttEnum.BROKER.value
    val gson = Gson()
    var mqttClient: MqttClient? = null
    var connOptions: MqttConnectOptions? = null


    @PostConstruct
    fun init() {
        val mqttClientId = MqttAsyncClient.generateClientId()
        this.mqttClient = MqttClient(broker, mqttClientId)
        connOptions = MqttConnectOptions()

        // configure connection options.
        connOptions!!.isCleanSession = false
        connOptions!!.isAutomaticReconnect = true
        connOptions!!.connectionTimeout = 10
        connOptions!!.userName = mqttUtils.mqttUser
        connOptions!!.password = mqttUtils.mqttPassword.toCharArray()
        connOptions!!.setWill(slotsTopic, "-1".toByteArray(), 1 , true)

        if (!this.mqttClient!!.isConnected) {
            try {
                this.mqttClient!!.connect(connOptions)
            } catch (e: MqttException) {
                println("Unable to connect MQTT client.")
            }
        }
    }



    fun publishSlots (slots: List<SlotsDTO>?): Boolean {
        val jsonSlots = gson.toJson(slots)
        return try {
            if (!this.mqttClient!!.isConnected) {
                this.mqttClient!!.connect(this.connOptions)
            }
            this.mqttClient?.publish(slotsTopic, jsonSlots.toByteArray(), 1, true)
            true
        } catch (e: Exception) {
            false
        }
    }

}