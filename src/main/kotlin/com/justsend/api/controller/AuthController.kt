package com.justsend.api.controller

import com.justsend.api.dto.RegisterDto
import com.justsend.api.service.AuthService
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
  fun registerUser(@RequestBody body: RegisterDto): ResponseEntity<String> {
    authService.registerUser(body)
    return ResponseEntity.ok("User registered successfully")
  }
}
