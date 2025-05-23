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

  public override fun doFilterInternal(
    request: HttpServletRequest,
    response: HttpServletResponse,
    filterChain: FilterChain
  ) {
    val authHeader: String? = request.getHeader("Authorization")

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response)
      return
    }

    val jwt = authHeader.substringAfter("Bearer ")
    val userId = jwtUtils.extractUserId(jwt)

    if (SecurityContextHolder.getContext().authentication == null) {
      val userDetails = userDetailsService.loadUserByUsername(userId)

      if (jwtUtils.isTokenValid(jwt, userDetails)) {
        val authToken =
          UsernamePasswordAuthenticationToken(
            userDetails,
            jwt,
            userDetails.authorities
          )
        authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
        SecurityContextHolder.getContext().authentication = authToken
      }
    }

    filterChain.doFilter(request, response)
  }
}
