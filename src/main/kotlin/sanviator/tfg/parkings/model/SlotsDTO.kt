package sanviator.tfg.parkings.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
data class SlotsDTO (
    @JsonProperty("idSlot") var idSlot: String? = null,
    @JsonProperty("idParking") var idParking: String? = null,
    @JsonProperty("timestamp") var timestamp: String? = null,
    @JsonProperty("totalSlots") var totalSlots: Int? = null,
    @JsonProperty("freeSlots") var freeSlots: Int? = null
) {
}