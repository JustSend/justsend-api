package com.justsend.api.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfig {

  @Bean
  fun passwordEncoder() = BCryptPasswordEncoder()

  @Bean
  fun filterChain(http: HttpSecurity): SecurityFilterChain {
    http
      .cors { }
      .csrf { it.disable() }
      .authorizeHttpRequests { auth ->
        auth.anyRequest().permitAll()
      }
    return http.build()
  }
}
