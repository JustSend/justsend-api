package com.justsend.api.service

import com.justsend.api.dto.RegisterDto
import com.justsend.api.entity.User
import com.justsend.api.entity.WalletEntity
import com.justsend.api.repository.UserRepository
import com.justsend.api.security.JwtService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
  private val userRepository: UserRepository,
  private val passwordEncoder: BCryptPasswordEncoder,
  private val jwtService: JwtService
) {

  fun register(dto: RegisterDto): Result<String> {
    if (userRepository.existsByEmail(dto.email)) {
      return Result.failure(IllegalArgumentException("Email '${dto.email}' is already registered"))
    }

    val user = User(
      email = dto.email,
      password = passwordEncoder.encode(dto.password),
      wallet = WalletEntity()
    )

    userRepository.save(user)
    return Result.success("User registered successfully")
  }

  fun login(email: String, password: String): User {
    val user = userRepository.findByEmail(email)
      ?: throw IllegalArgumentException("Invalid credentials")

    if (!passwordEncoder.matches(password, user.password)) {
      throw IllegalArgumentException("Invalid credentials")
    }

    return user
  }
}
