package sanviator.tfg.parkings.security.filter


import com.google.gson.Gson
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.ldap.userdetails.LdapUserDetailsImpl
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import sanviator.tfg.parkings.controllers.auth.responses.LoginDTO
import sanviator.tfg.parkings.controllers.auth.responses.PostTokens
import sanviator.tfg.parkings.services.AuthService
import java.io.BufferedReader
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class CustomAuthenticationFilter(authenticationManager: AuthenticationManager?, authService: AuthService) :
    UsernamePasswordAuthenticationFilter(authenticationManager) {


    private var authService = authService


    private var logger: Logger = LoggerFactory.getLogger(CustomAuthenticationFilter::class.java)
    private val gson = Gson()


    override fun attemptAuthentication(request: HttpServletRequest?, response: HttpServletResponse?): Authentication {
        var loginDTOObject: LoginDTO? = null
        if ("POST" == request!!.method) {
            val bodyOfRequest = request.reader.use(BufferedReader::readText)
            loginDTOObject = gson.fromJson(bodyOfRequest, LoginDTO::class.java)
        }
        val authenticationToken = UsernamePasswordAuthenticationToken(loginDTOObject?.user, loginDTOObject?.password)
        return authenticationManager.authenticate(authenticationToken)
    }


    override fun successfulAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        chain: FilterChain?,
        authentication: Authentication?
    ) {
        val user = authentication?.principal as LdapUserDetailsImpl

        val accessToken = authService.generateAccessTokenFromUsername(user.username)
        val refreshToken = authService.generateRefreshTokenFromUsername(user.username)

        // Process of sending the tokens through a DTO called "PostTokens"
        val jsonPostTokens = gson.toJson(PostTokens(accessToken, refreshToken))
        val out = response?.writer
        response?.contentType = "application/json"
        response?.characterEncoding = "UTF-8"
        out?.print(jsonPostTokens)
        out?.flush()
    }


    override fun unsuccessfulAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        failed: AuthenticationException?
    ) {
        logger.info("Authentication credentials not correct")
        response?.setHeader("error", "Incorrect credentials")
        response?.sendError(403, "Forbidden")

    }
}