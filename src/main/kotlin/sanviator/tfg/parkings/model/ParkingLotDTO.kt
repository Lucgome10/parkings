package sanviator.tfg.parkings.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ParkingLotDTO (
    @JsonProperty("id") var id: String? = null,
    @JsonProperty("name") var name: String? = null,
    @JsonProperty("city") var city: String? = null,
    @JsonProperty("lon") var lon: Double? = null,
    @JsonProperty("lat") var lat: Double? = null,
) {
}