package sanviator.tfg.parkings.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import sanviator.tfg.parkings.exception.ServiceException
import sanviator.tfg.parkings.mappers.compareTimestamp
import sanviator.tfg.parkings.mappers.toSlotsDAO
import sanviator.tfg.parkings.mappers.toSlotsDTO
import sanviator.tfg.parkings.model.SlotsDTO
import sanviator.tfg.parkings.persistence.repositories.ParkingLotsRepository
import sanviator.tfg.parkings.persistence.repositories.SlotsRepository



@Service
class SlotsService {

    @Autowired
    private lateinit var slotsRepository: SlotsRepository

    @Autowired
    private lateinit var parkingLotsRepository: ParkingLotsRepository


    // Returns all slots stored in database.
    fun getAllSlots(): ArrayList<SlotsDTO>? {
        return try {
            slotsRepository.findAll().map { slotsDAO ->
                slotsDAO.toSlotsDTO()
            }.toCollection(ArrayList())
        } catch (e: Exception) {
            throw ServiceException(e.message)
        }
    }


    // Returns most recent slot of each parking belonging to a saved parking.
    fun getLastSlots(): ArrayList<SlotsDTO>? {
        val recentSlots: ArrayList<SlotsDTO> = ArrayList()
        val parkingIds = parkingLotsRepository.findAll().map { it.id }

        val slotsGrouped = slotsRepository
            .findAll()
            .map { slotsDAO -> slotsDAO.toSlotsDTO() }  // Transform to SlotsDTO
            .toCollection(ArrayList())                  // puts objects in ArrayList
            .filter { it.idParking in parkingIds }      // get only those slots belonging to stored parkings
            .groupBy { it.idParking }                   // group slots by parking.

        for (key in slotsGrouped.keys) {
            var mostRecentSlot: SlotsDTO? = slotsGrouped.getValue(key)[0]
            for (slot in slotsGrouped.getValue(key)) {
                mostRecentSlot = mostRecentSlot?.let { slot.compareTimestamp(it) }
            }
            if (mostRecentSlot != null) {
                recentSlots.add(mostRecentSlot)
            }
        }
        return recentSlots
    }


    // Saves slots in table "slots"
    fun saveSlots(slots: SlotsDTO?): ResponseEntity<Any> {
        return try {
            if (slots != null) {
                slotsRepository.save(slots.toSlotsDAO())
            }
            ResponseEntity(HttpStatus.OK)
        } catch (e: Exception) {
            throw ServiceException(e.message)
        }
    }



    fun deleteGivenSlots(oldSlots: List<SlotsDTO>?): Boolean {
        if (oldSlots!=null) {
            try {
                slotsRepository.deleteAll(oldSlots.map { slotsDTO ->
                    slotsDTO.toSlotsDAO()
                })
            } catch (e: Exception) {
                return false
            }
            return true
        } else {
            println("No old slots to delete.")
            return false
        }
    }


}