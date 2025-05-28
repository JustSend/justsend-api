package com.justsend.api.config

import com.justsend.api.security.FirebaseAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class SecurityConfig {

  @Bean
  fun filterChain(http: HttpSecurity): SecurityFilterChain {
    http
      .csrf { it.disable() }
      .authorizeHttpRequests {
        it
          .requestMatchers("/user/**").permitAll()
          .anyRequest().authenticated()
      }
      .addFilterBefore(FirebaseAuthenticationFilter(), UsernamePasswordAuthenticationFilter::class.java)

    return http.build()
  }
}
