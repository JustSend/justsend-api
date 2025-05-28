package com.justsend.api.controller

import com.justsend.api.service.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController()
@RequestMapping("/user")
class UserController(
  private val authService: AuthService
) {

  @PostMapping("/{uid}")
  fun addUser(@PathVariable uid: String): ResponseEntity<String> {
    val result = authService.register(uid)
    return result.fold(
      onSuccess = { successMessage -> ResponseEntity.ok(successMessage) },
      onFailure = { error -> ResponseEntity.badRequest().body(error.message ?: "Unknown error") }
    )
  }
}
