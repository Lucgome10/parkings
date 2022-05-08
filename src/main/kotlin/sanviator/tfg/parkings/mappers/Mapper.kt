package sanviator.tfg.parkings.mappers

import sanviator.tfg.parkings.model.ParkingLotDTO
import sanviator.tfg.parkings.model.SlotsDTO
import sanviator.tfg.parkings.persistence.dao.ParkingLotDAO
import sanviator.tfg.parkings.persistence.dao.SlotsDAO
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


fun ParkingLotDAO.toParkingLotDTO() = ParkingLotDTO (
    id = id,
    name = name,
    city = city,
    lon = lon,
    lat = lat
)


fun ParkingLotDTO.toParkingLotDAO() = ParkingLotDAO (
    id = id,
    name = name,
    city = city,
    lon = lon,
    lat = lat
)


fun SlotsDAO.toSlotsDTO() = SlotsDTO (
    idSlot = idSlot,
    idParking = idParking,
    timestamp = timestamp,
    totalSlots = totalSlots,
    freeSlots = freeSlots
)

fun SlotsDTO.toSlotsDAO() = SlotsDAO(
    idSlot = idSlot,
    idParking = idParking,
    timestamp = timestamp,
    totalSlots = totalSlots,
    freeSlots = freeSlots
)


// returns most recent slot
fun SlotsDTO.compareTimestamp(otherSlot: SlotsDTO): SlotsDTO {
    val currentMillis = System.currentTimeMillis() // Gets current millis from system.
    val thisMillis = this.timestamp?.toEpochMilli()
    val otherMillis = this.timestamp?.toEpochMilli()

    return if ( (currentMillis- thisMillis!!) <= (currentMillis- otherMillis!!) ) this else otherSlot
}


fun String.toEpochMilli(): Long {
    val formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
    val timestamp = this
    val temporalAccessor = formatter.parse(timestamp)
    val localDateTime = LocalDateTime.from(temporalAccessor)
    val zonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.systemDefault())
    val instant = Instant.from(zonedDateTime)
    return instant.toEpochMilli()
}