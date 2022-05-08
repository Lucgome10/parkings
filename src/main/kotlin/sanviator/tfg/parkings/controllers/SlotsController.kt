package sanviator.tfg.parkings.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import sanviator.tfg.parkings.exception.ServiceException
import sanviator.tfg.parkings.model.SlotsDTO
import sanviator.tfg.parkings.services.MqttService
import sanviator.tfg.parkings.services.SlotsService




@RestController
@RequestMapping("slots_available")
class SlotsController {

    @Autowired
    private lateinit var slotsService: SlotsService

    @Autowired
    private lateinit var mqttService: MqttService


    // GET last slots.
    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getSlots(): ResponseEntity<List<SlotsDTO>> {
        return try {
            ResponseEntity(slotsService.getLastSlots(), HttpStatus.OK)
        } catch (e: ServiceException) {
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    // GET all stored slots
    @GetMapping("/all")
    fun getAllSlots(): ResponseEntity<List<SlotsDTO>> {
        return try {
            ResponseEntity(slotsService.getAllSlots(), HttpStatus.OK)
        } catch (e: ServiceException) {
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }


    // POST slots from just one parking.
    @PostMapping("")
    fun insert (@RequestBody slots: SlotsDTO?): ResponseEntity<Any> {
        return try {
            slotsService.saveSlots(slots)
//            mqttService.publishSlots(slots)
            ResponseEntity(HttpStatus.OK)
        } catch (e: ServiceException) {
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}
