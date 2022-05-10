package sanviator.tfg.parkings.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import sanviator.tfg.parkings.exception.ServiceException
import sanviator.tfg.parkings.model.ParkingLotDTO
import sanviator.tfg.parkings.services.ParkingLotsService

@RestController
@RequestMapping("parking_lots")
class ParkingLotsController {

    // to be implemented yet.
    @Autowired
    private lateinit var parkingLotsService: ParkingLotsService


    // GET all parking lots to be used in client.
    @GetMapping("")
    fun list(): ResponseEntity<List<ParkingLotDTO>> {
        return try {
            ResponseEntity(parkingLotsService.getAllParkingLots(), HttpStatus.OK)
        } catch (e: ServiceException) {
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }


    // POST all parking lots to be used in client.
    @PostMapping("")
    fun insert(@RequestBody parkingLots: List<ParkingLotDTO>?): ResponseEntity<Any> {
        return try {
            parkingLotsService.saveAllParkingLots(parkingLots)
            ResponseEntity(HttpStatus.OK)
        } catch (e: ServiceException) {
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}
