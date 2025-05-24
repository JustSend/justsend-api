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
  private val jwtUtils: JwtUtils,
  private val userDetailsService: CustomUserDetailsService
) : OncePerRequestFilter() {

  override fun doFilterInternal(
    request: HttpServletRequest,
    response: HttpServletResponse,
    filterChain: FilterChain
  ) {
    try {
      val authHeader: String? = request.getHeader("Authorization")
      println("Authorization header: $authHeader")

      if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        println("No JWT token found or bad format")
        filterChain.doFilter(request, response)
        return
      }

      val jwt = authHeader.substringAfter("Bearer ")
      println("JWT token extracted: $jwt")

      val userId = jwtUtils.extractUserId(jwt)
      println("User ID extracted from JWT: $userId")

      if (SecurityContextHolder.getContext().authentication == null) {
        val userDetails = userDetailsService.loadUserByUsername(userId)
        println("UserDetails loaded: $userDetails")

        if (jwtUtils.isTokenValid(jwt, userDetails)) {
          println("JWT token is valid")
          val authToken = UsernamePasswordAuthenticationToken(
            userDetails,
            jwt,
            userDetails.authorities
          )
          authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
          SecurityContextHolder.getContext().authentication = authToken
        } else {
          println("JWT token is invalid")
        }
      } else {
        println("Authentication already set in context")
      }
    } catch (ex: Exception) {
      println("JWT authentication failed: ${ex.message}")
      SecurityContextHolder.clearContext()
    }

    println("Authentication before filterChain.doFilter: ${SecurityContextHolder.getContext().authentication}")
    filterChain.doFilter(request, response)
  }
}
