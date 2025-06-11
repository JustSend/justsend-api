package com.justsend.api.controller

import com.justsend.api.dto.User
import com.justsend.api.dto.UserInfo
import com.justsend.api.service.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController()
@RequestMapping("/api/user")
class UserController(
  private val authService: AuthService
) {

  @PostMapping("/register")
  fun register(@RequestBody user: User): ResponseEntity<String> {
    val result = authService.register(user)
    return result.fold(
      onSuccess = { successMessage -> ResponseEntity.ok(successMessage) },
      onFailure = { error -> ResponseEntity.badRequest().body(error.message ?: "Unknown error") }
    )
  }

  @GetMapping("/me")
  fun getWalletInfo(): UserInfo {
    val user = authService.getUserWallet()
    return UserInfo(user.alias, user.email)
  }

  @GetMapping("/search")
  fun getAllOtherUsers(): List<UserInfo> {
    val currentUser = authService.getUserWallet()
    val allUsers = authService.getAllUsers()

    return allUsers
      .filter { it.email != currentUser.email }
      .map { UserInfo(alias = it.alias, email = it.email) }
  }
}
