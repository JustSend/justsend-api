package com.justsend.api.service

import com.justsend.api.dto.Amount
import com.justsend.api.dto.Currency
import com.justsend.api.dto.Money
import com.justsend.api.repository.WalletRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class WalletService(
  private val authService: AuthService,
  private val walletRepository: WalletRepository
) {

  fun getBalances(): Map<Currency, Amount> {
    val wallet = authService.getUserWallet()
    return wallet.getAllBalances()
  }

  fun getBalanceIn(currency: Currency): Amount? {
    val wallet = authService.getUserWallet()
    return wallet.getBalanceIn(currency)
  }

  @Transactional
  fun deposit(money: Money): Result<String> {
    return try {
      val wallet = authService.getUserWallet()

      val updatedWallet = wallet.add(money)

      walletRepository.save(updatedWallet)

      Result.success("Added ${money.amount} ${money.currency} to wallet successfully")
    } catch (ex: IllegalArgumentException) {
      Result.failure(ex)
    } catch (ex: Exception) {
      Result.failure(RuntimeException("Failed to process deposit: ${ex.message}", ex))
    }
  }

  fun withdraw(money: Money): Result<String> {
    return try {
      val wallet = authService.getUserWallet()

      val updatedWallet = wallet.remove(money)

      walletRepository.save(updatedWallet)

      Result.success("Added ${money.amount} ${money.currency} to wallet successfully")
    } catch (ex: IllegalArgumentException) {
      Result.failure(ex)
    } catch (ex: Exception) {
      Result.failure(RuntimeException("Failed to process deposit: ${ex.message}", ex))
    }
  }
}
