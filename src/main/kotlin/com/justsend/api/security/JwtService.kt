package com.justsend.api.security

import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.security.Key
import java.util.Date
import java.util.UUID

@Service
class JwtService(
  @Value("\${jwt.secret}")
  private val jwtSecret: String
) {

  private val key: Key = Keys.hmacShaKeyFor(jwtSecret.toByteArray())

  fun generateJwtToken(userId: UUID): String {
    val now = Date()
    val weekInMiliSeconds = 604800000
    val expiryDate = Date(now.time + weekInMiliSeconds)

    return Jwts.builder()
      .setSubject(userId.toString())
      .setIssuedAt(now)
      .setExpiration(expiryDate)
      .signWith(key, SignatureAlgorithm.HS256)
      .compact()
  }

  fun extractUserId(token: String): String {
    val claims = Jwts.parserBuilder()
      .setSigningKey(key)
      .build()
      .parseClaimsJws(token)
      .body
    return claims.subject
  }

  fun validateJwtToken(authToken: String): Boolean {
    try {
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken)
      return true
    } catch (e: SecurityException) {
      println("Invalid JWT signature: ${e.message}")
    } catch (e: MalformedJwtException) {
      println("Invalid JWT token: ${e.message}")
    } catch (e: ExpiredJwtException) {
      println("JWT token is expired: ${e.message}")
    } catch (e: UnsupportedJwtException) {
      println("JWT token is unsupported: ${e.message}")
    } catch (e: IllegalArgumentException) {
      println("JWT claims string is empty: ${e.message}")
    }
    return false
  }

  fun isTokenValid(token: String, userDetails: UserDetails): Boolean {
    val userId = extractUserId(token)
    return (userId == userDetails.username) && validateJwtToken(token)
  }
}
