package com.justsend.api.config

import com.justsend.api.security.JwtAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig(
  private val jwtAuthFilter: JwtAuthenticationFilter,
  private val userDetailsService: UserDetailsService
) {

  @Bean
  fun passwordEncoder() = BCryptPasswordEncoder()

  @Bean
  fun authenticationProvider(): DaoAuthenticationProvider {
    val provider = DaoAuthenticationProvider()
    provider.setUserDetailsService(userDetailsService)
    provider.setPasswordEncoder(passwordEncoder())
    return provider
  }

  @Bean
  fun authenticationManager(authConfig: AuthenticationConfiguration): AuthenticationManager =
    authConfig.authenticationManager

  @Bean
  fun filterChain(http: HttpSecurity): SecurityFilterChain {
    http
      .cors { }
      .csrf { it.disable() }
      .authorizeHttpRequests {
        it.requestMatchers("/auth/**").permitAll()
          .anyRequest().authenticated()
      }
      .authenticationProvider(authenticationProvider())
      .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)

    return http.build()
  }

  @Bean
  fun corsConfigurationSource(): CorsConfigurationSource {
    val configuration = CorsConfiguration()
    configuration.allowedOrigins = listOf("*")
    configuration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
    configuration.allowedHeaders = listOf("*")
    configuration.allowCredentials = true
    val source = UrlBasedCorsConfigurationSource()
    source.registerCorsConfiguration("/**", configuration)
    return source
  }
}
