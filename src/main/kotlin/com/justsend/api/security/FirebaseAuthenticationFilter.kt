package com.justsend.api.security

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseToken
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class FirebaseAuthenticationFilter : OncePerRequestFilter() {
  override fun doFilterInternal(
    request: HttpServletRequest,
    response: HttpServletResponse,
    filterChain: FilterChain
  ) {
    val header = request.getHeader("Authorization")

    if (header != null && header.startsWith("Bearer ")) {
      val idToken = header.removePrefix("Bearer ").trim()

      try {
        val decodedToken: FirebaseToken = FirebaseAuth.getInstance().verifyIdToken(idToken)
        val uid = decodedToken.uid

        val authentication = UsernamePasswordAuthenticationToken(uid, null, emptyList())
        SecurityContextHolder.getContext().authentication = authentication
      } catch (_: Exception) {
        response.status = HttpServletResponse.SC_UNAUTHORIZED
        return
      }
    }

    filterChain.doFilter(request, response)
  }
}
