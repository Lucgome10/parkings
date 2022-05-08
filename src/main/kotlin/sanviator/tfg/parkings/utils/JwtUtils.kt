package sanviator.tfg.parkings.utils

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component


@Component
class JwtUtils {

    @Value("\${app.jwtSecret}")
    lateinit var jwtSecret: String


    @Value("\${app.jwtExpirationMs}")
    lateinit var jwtAccessExpirationMs: String


    @Value("\${app.jwtRefreshExpirationMs}")
    lateinit var jwtRefreshExpirationMs: String

}