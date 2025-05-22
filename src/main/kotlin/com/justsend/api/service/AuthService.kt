package com.justsend.api.service

import com.justsend.api.dto.RegisterDto
import com.justsend.api.entity.User
import com.justsend.api.repository.UserRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class AuthService(
  private val userRepository: UserRepository,
  private val passwordEncoder: BCryptPasswordEncoder
) {

  fun registerUser(dto: RegisterDto) {
    if (userRepository.existsByEmail(dto.email)) {
      throw IllegalArgumentException("Username already taken")
    }

    val hashedPassword = passwordEncoder.encode(dto.password)

    val walletId = UUID.randomUUID()

    val user = User(
      email = dto.email,
      password = hashedPassword,
      walletId = walletId
    )

    userRepository.save(user)
  }
}
