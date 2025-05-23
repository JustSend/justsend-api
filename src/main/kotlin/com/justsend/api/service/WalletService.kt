package com.justsend.api.service

import com.justsend.api.model.Amount
import com.justsend.api.model.Currency
import com.justsend.api.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class WalletService(
  private val userRepository: UserRepository,
  private val securityService: SecurityService
) {

  fun getBalances(): Map<Currency, Amount> {
    val userId = securityService.getUserId()

    val user = userRepository.findById(userId)
      .orElseThrow { IllegalStateException("User not found") }

    return user.wallet.balances
  }
}
