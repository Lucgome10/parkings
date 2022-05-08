package sanviator.tfg.parkings.controllers.auth

import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import com.google.gson.Gson
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import sanviator.tfg.parkings.controllers.auth.responses.RefreshTokenDTO
import sanviator.tfg.parkings.services.AuthService
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@RestController
@RequestMapping("/auth")
class AuthController {

    private var logger: Logger = LoggerFactory.getLogger(AuthController::class.java)
    private val gson = Gson()

    @Autowired
    private lateinit var authService: AuthService

    // obtains new access and refresh tokens from JSON. It does not check authorization header at all.
    @PostMapping("/refresh_token")
    fun refreshToken(@RequestBody refreshTokenDTO: RefreshTokenDTO?): ResponseEntity<Any> {
        if (refreshTokenDTO != null) {
            val tokenReceived = refreshTokenDTO.refreshToken
            val authorizationHeaderFromTokenReceived = "Bearer " + tokenReceived
            try {
                // generate new tokens through service
                val postTokens = authService.generatePostTokens(authorizationHeaderFromTokenReceived)
                return if (postTokens != null) {
                    ResponseEntity(postTokens, HttpStatus.OK)
                } else {
                    logger.info("Unable to decode JWT token.")
                    ResponseEntity("Access token is missing or invalid", HttpStatus.UNAUTHORIZED)
                }
            } catch (e: Exception) {
                logger.info("Internal exception " + e.message)
                return  ResponseEntity("Access token is missing or invalid", HttpStatus.UNAUTHORIZED)
            }
        } else {
            logger.info("Refresh token is missing.")
            return ResponseEntity("Access token is missing or invalid", HttpStatus.UNAUTHORIZED)
        }
    }


    @GetMapping("/logout")
    fun logout(request: HttpServletRequest, response: HttpServletResponse): Unit {
        val authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION)
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                val validToken = authService.verifyToken(authorizationHeader)
                if (validToken==false) {
                    response.setStatus(401, "Access token is missing or invalid")
                }
            }catch (e: TokenExpiredException) {
                logger.info("Token expired " + e.message)
                response.setHeader("error", "tokenExpired")
                response.setStatus(401, "Access token is missing or invalid")
            } catch (e: JWTVerificationException) {
                logger.info("Unable to verify token " + e.message)
                response.setHeader("error", "tokenInvalid")
                response.setStatus(401, "Access token is missing or invalid")
            } catch (e: Exception) {
                logger.info("Internal exception " + e.message)
                response.setHeader("error", e.message)
                response.setStatus(401, "Access token is missing or invalid")
            }
        } else {
            response.setStatus(401, "Access token is missing or invalid")
        }
    }
}