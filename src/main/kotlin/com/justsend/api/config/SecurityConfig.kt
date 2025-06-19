package com.justsend.api.config

import com.justsend.api.security.FirebaseAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
class SecurityConfig {

  @Bean
  fun filterChain(http: HttpSecurity): SecurityFilterChain {
    http
      .cors { }
      .csrf { it.disable() }
      .authorizeHttpRequests {
        it
          .requestMatchers("/api/user/register").permitAll()
          .requestMatchers("/api/wallet/deposit").permitAll()
          .anyRequest().authenticated()
      }
      .addFilterBefore(FirebaseAuthenticationFilter(), UsernamePasswordAuthenticationFilter::class.java)

    return http.build()
  }

  @Bean
  fun corsConfigurationSource(): CorsConfigurationSource {
    val configuration =
      CorsConfiguration().apply {
        allowedOrigins = listOf("http://localhost:8081", "http://localhost:8000")
        allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
        allowedHeaders = listOf("*")
        allowCredentials = true
      }
    val source = UrlBasedCorsConfigurationSource()
    source.registerCorsConfiguration("/**", configuration)
    return source
  }
}
