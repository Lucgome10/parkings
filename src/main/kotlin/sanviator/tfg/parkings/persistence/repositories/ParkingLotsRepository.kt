package sanviator.tfg.parkings.persistence.repositories

import org.springframework.data.jpa.repository.JpaRepository
import sanviator.tfg.parkings.persistence.dao.ParkingLotDAO

internal interface ParkingLotsRepository : JpaRepository<ParkingLotDAO, String>{
}