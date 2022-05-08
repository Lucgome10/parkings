package sanviator.tfg.parkings.services

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.TokenExpiredException
import com.auth0.jwt.interfaces.DecodedJWT
import com.google.gson.Gson
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import sanviator.tfg.parkings.controllers.auth.responses.PostTokens
import sanviator.tfg.parkings.utils.JwtUtils
import java.security.SignatureException
import java.util.*
import javax.annotation.PostConstruct


@Service
class AuthService {

    @Autowired
    lateinit var jwtUtils: JwtUtils


    private var algorithm: Algorithm? = null
    private var logger: Logger = LoggerFactory.getLogger(AuthService::class.java)
    private val gson = Gson()



    @PostConstruct
    fun init() {
        algorithm = Algorithm.HMAC512(jwtUtils.jwtSecret)
    }



    fun generateAccessTokenFromUsername(username: String?): String {
        return try {
            JWT.create()
                .withSubject(username)
                .withExpiresAt(Date(System.currentTimeMillis() + jwtUtils.jwtAccessExpirationMs.toInt())) // ACCESS TOKEN DURATION
                .sign(algorithm)
        } catch (e: Exception) {
            "exception creating AccessToken from username" + e.message
        }
    }

    fun generateRefreshTokenFromUsername(username: String?): String {
        return JWT.create()
            .withSubject(username)
            .withExpiresAt(Date(System.currentTimeMillis() + jwtUtils.jwtRefreshExpirationMs.toLong())) // ACCESS TOKEN DURATION
            .sign(algorithm)
    }

    fun verifyJwtToken(authToken: String): DecodedJWT? {
        try {
            return JWT.require(algorithm).build().verify(authToken)
        } catch (e: SignatureException) {
            logger.error("Invalid JWT signature: {}", e.message)
        } catch (e: TokenExpiredException) {
            logger.error("Token has expired",  e.message)
        } catch (e: IllegalArgumentException) {
            logger.error("JWT claims string is empty: {}", e.message)
        } catch (e: com.auth0.jwt.exceptions.JWTVerificationException) {
            logger.error("JWT Verification has failed.", e.message)
        }
        return null
    }


    fun generatePostTokens(authorizationHeader: String): PostTokens? {
        val algorithm = Algorithm.HMAC512(jwtUtils.jwtSecret)
        val refreshToken = authorizationHeader.substring("Bearer ".length) // Obtain token from Authorization header
        val decodedJWT = verifyJwtToken(refreshToken) // verify whether it is a valid token.
        // check whether refreshToken received was valid. If not null it is valid.
        if (decodedJWT!=null) {
            val username = decodedJWT.subject // extract username from token
            // generate new access & refresh tokens for the user
            val accessToken = generateAccessTokenFromUsername(username)
            val newRefreshToken = generateRefreshTokenFromUsername(username)

            return PostTokens(accessToken, newRefreshToken)
        } else {
            logger.info("Refresh token received was not valid")
            return null
        }

    }


    fun verifyToken(authorizationHeader: String): Boolean? {
        val algorithm = Algorithm.HMAC512(jwtUtils.jwtSecret)
        val refreshToken = authorizationHeader.substring("Bearer ".length) // Obtain token from Authorization header
        val decodedJWT = verifyJwtToken(refreshToken) // verify whether it is a valid token.
        return decodedJWT!=null
    }


}