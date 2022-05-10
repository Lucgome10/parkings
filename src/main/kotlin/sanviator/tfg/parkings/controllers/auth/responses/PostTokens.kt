package sanviator.tfg.parkings.controllers.auth.responses

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
data class PostTokens(
    @JsonProperty("accessToken")    var accessToken: String? = null,
    @JsonProperty("refreshToken")   var refreshToken: String? = null ){
}