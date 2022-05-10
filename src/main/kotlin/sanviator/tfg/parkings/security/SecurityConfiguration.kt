package sanviator.tfg.parkings.security

import com.auth0.jwt.algorithms.Algorithm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import sanviator.tfg.parkings.security.filter.CustomAuthenticationFilter
import sanviator.tfg.parkings.security.filter.CustomAuthorizationFilter
import sanviator.tfg.parkings.services.AuthService
import sanviator.tfg.parkings.utils.JwtUtils

@Configuration
class SecurityConfiguration : WebSecurityConfigurerAdapter() {

    @Autowired
    lateinit var jwtUtils: JwtUtils

    @Autowired
    lateinit var authService: AuthService


    @Bean
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }


    override fun configure(auth: AuthenticationManagerBuilder) {
        auth
            .ldapAuthentication()
            .userDnPatterns("uid={0},ou=people")
            .groupSearchBase("ou=groups")
            .contextSource()
            .url("ldap://localhost:8389/dc=springframework,dc=org")
            .and()
            .passwordCompare()
            .passwordEncoder(BCryptPasswordEncoder())
            .passwordAttribute("userPassword")
    }


    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        val algorithm = Algorithm.HMAC512(jwtUtils.jwtSecret)

        val customAuthenticationFilter = CustomAuthenticationFilter(authenticationManagerBean(), authService)
        customAuthenticationFilter.setFilterProcessesUrl("/auth/login")

        http.csrf().disable()
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        http.authorizeRequests().antMatchers("/auth/login/**", "/auth/refresh_token").permitAll()
//        http.authorizeRequests().antMatchers(HttpMethod.POST, "/locations").permitAll()
//        http.authorizeRequests().antMatchers(HttpMethod.POST, "/topology").permitAll()
        http.authorizeRequests().anyRequest().authenticated()
        http.addFilter(customAuthenticationFilter)
        http.addFilterBefore(CustomAuthorizationFilter(algorithm), UsernamePasswordAuthenticationFilter::class.java)
    }
}