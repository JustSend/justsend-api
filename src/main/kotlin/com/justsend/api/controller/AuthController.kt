package com.justsend.api.controller

import com.justsend.api.dto.LoginDto
import com.justsend.api.dto.LoginResponse
import com.justsend.api.dto.RegisterDto
import com.justsend.api.service.AuthService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
  private val authService: AuthService
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
  fun login(@RequestBody body: LoginDto): ResponseEntity<LoginResponse> {
    return try {
      val loginResponse = authService.login(body.email, body.password)
      ResponseEntity.ok(loginResponse)
    } catch (_: IllegalArgumentException) {
      ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
    }
  }
}
