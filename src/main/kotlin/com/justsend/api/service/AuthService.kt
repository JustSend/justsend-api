package com.justsend.api.service

import com.justsend.api.dto.LoginResponse
import com.justsend.api.dto.RegisterDto
import com.justsend.api.entity.User
import com.justsend.api.entity.WalletEntity
import com.justsend.api.repository.UserRepository
import com.justsend.api.repository.WalletRepository
import com.justsend.api.security.JwtUtils
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
  private val userRepository: UserRepository,
  private val walletRepository: WalletRepository,
  private val passwordEncoder: BCryptPasswordEncoder,
  private val jwtUtils: JwtUtils
) {

  fun register(dto: RegisterDto): Result<String> {
    if (userRepository.existsByEmail(dto.email)) {
      return Result.failure(IllegalArgumentException("Email '${dto.email}' is already registered"))
    }

    val emptyWallet = WalletEntity(
      balances = emptyMap()
    )

    val newWallet = walletRepository.save(emptyWallet)

    val user = User(
      email = dto.email,
      password = passwordEncoder.encode(dto.password),
      walletId = newWallet.id!!
    )

    val savedUser = userRepository.save(user)
    return Result.success(savedUser.id.toString())
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
