package com.justsend.api.service

import com.justsend.api.dto.LoginResponse
import com.justsend.api.dto.RegisterDto
import com.justsend.api.entity.User
import com.justsend.api.repository.UserRepository
import com.justsend.api.security.JwtUtils
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class AuthService(
  private val userRepository: UserRepository,
  private val passwordEncoder: BCryptPasswordEncoder,
  private val jwtUtils: JwtUtils
) {

  fun register(dto: RegisterDto) {
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

  fun login(email: String, password: String): LoginResponse {
    val user = userRepository.findByEmail(email)
      ?: throw IllegalArgumentException("Invalid credentials")

    if (!passwordEncoder.matches(password, user.password)) {
      throw IllegalArgumentException("Invalid credentials")
    }

    val token = jwtUtils.generateJwtToken(user.id!!)

    return LoginResponse(token = token)
  }
}
