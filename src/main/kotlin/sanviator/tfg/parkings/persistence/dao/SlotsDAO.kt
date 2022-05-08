package sanviator.tfg.parkings.persistence.dao

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "slots")
data class SlotsDAO (
    @Id @Column(name = "idSlot", nullable = false) var idSlot: String? = null,
    @Column(name = "idParking", nullable = false) var idParking: String? = null,
    @Column(name = "timestamp", nullable = false) var timestamp: String? = null,
    @Column(name = "totalSlots", nullable = false) var totalSlots: Int? = null,
    @Column (name = "freeSlots", nullable = false) var freeSlots: Int? = null
    ) {
}