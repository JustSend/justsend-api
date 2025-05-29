package com.justsend.api.service

import com.justsend.api.dto.Amount
import com.justsend.api.dto.Currency
import com.justsend.api.dto.Money
import com.justsend.api.dto.TransactionType
import com.justsend.api.mappers.WalletMapper
import org.springframework.stereotype.Service

@Service
class WalletService(
  private val userService: UserService,
  private val transactionService: TransactionService,
  private val authService: AuthService,
  private val walletMapper: WalletMapper
) {

  fun getBalances(): Map<Currency, Amount> {
    val user = authService.getAuthenticatedUser()
    return user.wallet.balances
  }

  fun deposit(money: Money): Result<String> {
    val user = authService.getAuthenticatedUser()

    return try {
      val wallet = walletMapper.toDomain(user.wallet)
      val updatedWallet = wallet.add(money)

      walletMapper.updateEntityFromDomain(updatedWallet, user.wallet)

      transactionService.createTransaction(user.wallet, money, TransactionType.DEPOSIT)

      Result.success("Added ${money.amount} ${money.currency} to wallet successfully")
    } catch (ex: IllegalArgumentException) {
      Result.failure(ex)
    }
  }

  fun withdraw(money: Money) {
    val user = authService.getAuthenticatedUser()
    val updatedWallet = walletMapper.toDomain(user.wallet).remove(money)
    userService.updateWallet(user, walletMapper.toEntity(updatedWallet))
    transactionService.createTransaction(user.wallet, money, TransactionType.EXTRACTION)
  }
}
