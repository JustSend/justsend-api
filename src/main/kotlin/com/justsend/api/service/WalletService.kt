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
  private val securityService: SecurityService
) {

  fun getBalances(): Map<Currency, Amount> {
    val user = securityService.getUser()
    return user.wallet.balances
  }

  fun addBalance(money: Money): Result<String> {
    val user = securityService.getUser()

    return try {
      val updatedWallet = user.wallet.toDomain().add(money)
      user.wallet = updatedWallet.toEntity()
      userRepository.save(user)
      Result.success("Added ${money.amount} ${money.currency} to wallet successfully")
    } catch (ex: IllegalArgumentException) {
      Result.failure(ex)
    }
  }

  fun removeBalance(money: Money) {
    val user = securityService.getUser()
    val updatedWallet = user.wallet.toDomain().remove(money)
    user.wallet = updatedWallet.toEntity()
    userRepository.save(user)
  }
}
