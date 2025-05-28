package com.justsend.api.controller

import com.justsend.api.dto.LoginDto
import com.justsend.api.dto.RegisterDto
import com.justsend.api.security.JwtService
import com.justsend.api.service.AuthService
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
  private val authService: AuthService,
  private val jwtService: JwtService
) {

  @PostMapping("/register")
  fun registerUser(@RequestBody dto: RegisterDto): ResponseEntity<Any> {
    val result = authService.register(dto)

    return result.fold(
      onSuccess = { successMessage -> ResponseEntity.ok(successMessage) },
      onFailure = { error -> ResponseEntity.badRequest().body(error.message ?: "Unknown error") }
    )
  }

  @PostMapping("/login")
  fun login(@RequestBody body: LoginDto, response: HttpServletResponse): ResponseEntity<Any> {
    val user = authService.login(body.email, body.password)

    val token = jwtService.generateJwtToken(user.id!!)

    val cookie = Cookie("token", token).apply {
      isHttpOnly = true
      secure = true
      path = "/"
      maxAge = 24 * 60 * 60
    }
    response.addCookie(cookie)
    return ResponseEntity.ok(mapOf("message" to "Logged in"))
  }
}
