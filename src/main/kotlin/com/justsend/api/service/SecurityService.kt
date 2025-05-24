package com.justsend.api.service

import com.justsend.api.entity.User
import com.justsend.api.repository.UserRepository
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class SecurityService(
  private val userRepository: UserRepository
) {

  fun getUser(): User {
    val authentication = SecurityContextHolder.getContext().authentication

    if (authentication == null || !authentication.isAuthenticated) {
      throw IllegalArgumentException("User is not authenticated")
    }

    val userId = UUID.fromString(authentication.name)
    return userRepository.findById(userId)
      .orElseThrow { UsernameNotFoundException("User not found: $userId") }
  }
}
