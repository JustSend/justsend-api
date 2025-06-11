package com.justsend.api.controller

import com.justsend.api.dto.User
import com.justsend.api.service.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController()
@RequestMapping("/user")
class UserController(
  private val authService: AuthService
) {

  @PostMapping
  fun addUser(@RequestBody user: User): ResponseEntity<String> {
    val result = authService.register(user)
    return result.fold(
      onSuccess = { successMessage -> ResponseEntity.ok(successMessage) },
      onFailure = { error -> ResponseEntity.badRequest().body(error.message ?: "Unknown error") }
    )
  }

  @GetMapping
  fun test() = "test"
}
