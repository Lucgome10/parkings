package sanviator.tfg.parkings.persistence.dao

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name="parking")
data class ParkingLotDAO (
    @Id @Column(name = "id", nullable = false) var id: String? = null,
    @Column (name = "name", nullable = false) var name: String? = null,
    @Column (name = "city", nullable = false) var city: String? = null,
    @Column (name = "lon", nullable = false) var lon: Double? = null,
    @Column (name = "lat", nullable = false) var lat: Double? = null) {
}