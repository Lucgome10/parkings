package sanviator.tfg.parkings.controllers.auth.responses

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
data class LoginDTO (
    @JsonProperty("user")       var user: String? = null,
    @JsonProperty("password")   var password: String? = null ){
}