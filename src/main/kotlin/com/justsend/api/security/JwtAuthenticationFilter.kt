package com.justsend.api.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
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
    val token = parseJwt(request)
    if (token != null && jwtUtils.validateJwtToken(token)) {
      val userId = jwtUtils.getUserIdFromJwtToken(token)
      val userDetails = userDetailsService.loadUserByUsername(userId)

      val auth = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
      SecurityContextHolder.getContext().authentication = auth
    }

    filterChain.doFilter(request, response)
  }

  private fun parseJwt(request: HttpServletRequest): String? {
    val headerAuth = request.getHeader("Authorization")
    return if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
      headerAuth.substring(7)
    } else {
      null
    }
  }
}
