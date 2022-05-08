package sanviator.tfg.parkings.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import sanviator.tfg.parkings.exception.ServiceException
import sanviator.tfg.parkings.mappers.toParkingLotDAO
import sanviator.tfg.parkings.mappers.toParkingLotDTO
import sanviator.tfg.parkings.model.ParkingLotDTO
import sanviator.tfg.parkings.persistence.repositories.ParkingLotsRepository


@Service
class ParkingLotsService {

    @Autowired
    private lateinit var parkingLotsRepository: ParkingLotsRepository


    // Used in GET /parking_lots
    fun getAllParkingLots(): ArrayList<ParkingLotDTO>? {
        return try {
            parkingLotsRepository.findAll().map { parkingLotDAO ->
                parkingLotDAO.toParkingLotDTO()
            }.toCollection(ArrayList())
        } catch (e: Exception) {
            throw ServiceException(e.message)
        }
    }


    // Used in POST /parking_lots
    fun saveAllParkingLots(parkingLots: List<ParkingLotDTO>?): ResponseEntity<Any> {
        return try {
            if (parkingLots != null) {
                parkingLotsRepository.saveAll(parkingLots.map { parkingLotDTO ->
                    parkingLotDTO.toParkingLotDAO()
                })
            }
            ResponseEntity(HttpStatus.OK)
        } catch (e: Exception) {
            throw ServiceException(e.message)
        }
    }



}