package com.justsend.api.service

import com.justsend.api.entity.User
import com.justsend.api.repository.UserRepository
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class SecurityService(
  private val userRepository: UserRepository
) {

  fun getUser(): User {
    val authentication = SecurityContextHolder.getContext().authentication

    if (authentication == null || !authentication.isAuthenticated) {
      throw IllegalArgumentException("User is not authenticated")
    }

    val email = authentication.name
    return userRepository.findByEmail(email)
      ?: throw UsernameNotFoundException("User not found: $email")
  }
}
