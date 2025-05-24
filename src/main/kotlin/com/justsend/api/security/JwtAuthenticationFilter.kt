package com.justsend.api.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
  private val jwtService: JwtService,
  private val userDetailsService: CustomUserDetailsService
) : OncePerRequestFilter() {

  override fun doFilterInternal(
    request: HttpServletRequest,
    response: HttpServletResponse,
    filterChain: FilterChain
  ) {
    try {
      val authHeader: String? = request.getHeader("Authorization")

      if (authHeader.isNullOrBlank() || !authHeader.startsWith("Bearer ")) {
        filterChain.doFilter(request, response)
        return
      }

      val jwt = authHeader.substringAfter("Bearer ").trim()
      val userId = jwtService.extractUserId(jwt)

      if (SecurityContextHolder.getContext().authentication == null) {
        val userDetails = userDetailsService.loadUserByUsername(userId)

        if (jwtService.isTokenValid(jwt, userDetails)) {
          val authToken = UsernamePasswordAuthenticationToken(
            userDetails,
            jwt,
            userDetails.authorities
          )
          authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
          SecurityContextHolder.getContext().authentication = authToken
        }
      }
    } catch (ex: Exception) {
      ex.printStackTrace()
    }

    filterChain.doFilter(request, response)
  }
}
