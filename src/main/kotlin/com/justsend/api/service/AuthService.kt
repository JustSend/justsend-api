package com.justsend.api.service

import com.justsend.api.dto.User
import com.justsend.api.entity.Wallet
import com.justsend.api.repository.WalletRepository
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class AuthService(
  private val walletRepository: WalletRepository
) {

  fun register(user: User): Result<String> {
    if (walletRepository.existsById(user.uid)) {
      return Result.failure(IllegalArgumentException("User '${user.email}' is already registered"))
    }

    val wallet = Wallet(
      id = user.uid,
      email = user.email
    )

    walletRepository.save(wallet)
    return Result.success("Wallet created successfully")
  }

  fun getUserWallet(): Wallet {
    val authentication = SecurityContextHolder.getContext().authentication
    val uid = authentication?.principal as? String
      ?: throw IllegalStateException("Firebase UID not found in security context")

    return walletRepository.findById(uid)
      .orElseThrow { IllegalStateException("User with UID $uid not found") }
  }

  fun getAllUsers(): List<Wallet> {
    return walletRepository.findAll()
  }
}
