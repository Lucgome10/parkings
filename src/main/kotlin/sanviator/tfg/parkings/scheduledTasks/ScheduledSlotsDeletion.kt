package sanviator.tfg.parkings.scheduledTasks

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import sanviator.tfg.parkings.mappers.toEpochMilli
import sanviator.tfg.parkings.services.SlotsService

@Component
class ScheduledSlotsDeletion {

    @Autowired
    private lateinit var slotsService: SlotsService

    private val timeToDelete = 300000 // 5 minutes.



    @Scheduled(fixedRate = 300000) // deletes past entries every 5 minutes
    fun deletePastSlots() {
        val currentTimeMillis = System.currentTimeMillis()
        val slots = slotsService.getAllSlots()
        val oldSlots = slots?.filter {
            (currentTimeMillis - it.timestamp!!.toEpochMilli()) > timeToDelete
        }
        slotsService.deleteGivenSlots(oldSlots)
    }


}