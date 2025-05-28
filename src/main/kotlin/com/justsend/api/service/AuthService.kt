package com.justsend.api.service

import com.justsend.api.entity.User
import com.justsend.api.entity.WalletEntity
import com.justsend.api.repository.UserRepository
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class AuthService(
  private val userRepository: UserRepository
) {

  fun register(uid: String): Result<String> {
    if (userRepository.existsById(uid)) {
      return Result.failure(IllegalArgumentException("User '$uid' is already registered"))
    }

    val user = User(
      id = uid,
      wallet = WalletEntity()
    )

    userRepository.save(user)
    return Result.success("User registered successfully")
  }

  fun getAuthenticatedUser(): User {
    val authentication = SecurityContextHolder.getContext().authentication
    val uid = authentication?.principal as? String
      ?: throw IllegalStateException("Firebase UID not found in security context")

    return userRepository.findById(uid)
      .orElseThrow { IllegalStateException("User with UID $uid not found") }
  }
}
