package com.justsend.api.service

import com.justsend.api.dto.Amount
import com.justsend.api.dto.Currency
import com.justsend.api.dto.Money
import com.justsend.api.mappers.toDomain
import com.justsend.api.mappers.toEntity
import com.justsend.api.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class WalletService(
  private val userRepository: UserRepository,
  securityService: SecurityService
) {
  private val userId = securityService.getUserId()

  private val user = userRepository.findById(userId)
    .orElseThrow { IllegalStateException("User not found") }

  fun getBalances(): Map<Currency, Amount> {
    return user.wallet.balances
  }

  fun addBalance(money: Money) {
    val updatedWallet = user.wallet.toDomain().add(money)
    user.wallet = updatedWallet.toEntity()
    userRepository.save(user)
  }

  fun removeBalance(money: Money) {
    val updatedWallet = user.wallet.toDomain().remove(money)
    user.wallet = updatedWallet.toEntity()
    userRepository.save(user)
  }
}
