package sanviator.tfg.parkings.scheduledTasks

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import sanviator.tfg.parkings.services.MqttService
import sanviator.tfg.parkings.services.SlotsService


@Component
class ScheduledMqttPublish {

    @Autowired
    private lateinit var mqttService: MqttService

    @Autowired
    private lateinit var slotsService: SlotsService


    // publish every 10 seconds.
    @Scheduled (fixedRate = 10000)
    fun publishSlotsMqtt() {
        val lastSlots = slotsService.getLastSlots()
        mqttService.publishSlots(lastSlots)
    }


}