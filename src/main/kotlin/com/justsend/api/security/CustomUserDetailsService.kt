package com.justsend.api.security

import com.justsend.api.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class CustomUserDetailsService(
  private val userRepository: UserRepository
) : UserDetailsService {
  override fun loadUserByUsername(userId: String): UserDetails {
    val uuid = UUID.fromString(userId)
    val user = userRepository.findById(uuid)
      .orElseThrow { UsernameNotFoundException("User not found with id: $userId") }
    return UserPrincipal(user.id!!, user.email)
  }
}
