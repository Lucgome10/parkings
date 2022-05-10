package sanviator.tfg.parkings.security.filter

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class CustomAuthorizationFilter(algorithm: Algorithm): OncePerRequestFilter() {

    private var logger: Logger = LoggerFactory.getLogger(CustomAuthorizationFilter::class.java)
    private val algorithm = algorithm

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        if (request.servletPath.equals("/auth/login") || request.servletPath.equals("/auth/refresh_token")) {
            filterChain.doFilter(request, response)
        } else {
            val authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION)
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                try {
                    val token = authorizationHeader.substring("Bearer ".length)
                    val verifier = JWT.require(algorithm).build()
                    val decodedJWT = verifier.verify(token)
                    val username = decodedJWT.subject
                    val authenticationToken = UsernamePasswordAuthenticationToken(username, null, null)
                    SecurityContextHolder.getContext().authentication = authenticationToken
                    filterChain.doFilter(request, response)
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
                logger.info("Token is missing or has wrong format.")
                response.setHeader("error", "formatError")
                response.setStatus(401, "Access token is missing or invalid")
//                filterChain.doFilter(request, response)
            }
        }
    }
}