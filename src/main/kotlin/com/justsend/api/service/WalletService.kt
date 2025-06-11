package com.justsend.api.service

import com.justsend.api.dto.Amount
import com.justsend.api.dto.Currency
import com.justsend.api.dto.Money
import com.justsend.api.dto.P2PUser
import com.justsend.api.dto.TransactionDto
import com.justsend.api.dto.TransactionType
import com.justsend.api.dto.toDto
import com.justsend.api.entity.Wallet
import com.justsend.api.repository.WalletRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import kotlin.collections.map

@Service
class WalletService(
  private val authService: AuthService,
  private val transactionService: TransactionService,
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
  fun deposit(money: Money, wallet: Wallet): Result<String> {
    return try {
      val updatedWallet = wallet.add(money)

      transactionService.createTransaction(wallet, money, TransactionType.DEPOSIT)

      walletRepository.save(updatedWallet)

      Result.success("Added ${money.amount} ${money.currency} to wallet successfully")
    } catch (ex: IllegalArgumentException) {
      Result.failure(ex)
    } catch (ex: Exception) {
      Result.failure(RuntimeException("Failed to process deposit: ${ex.message}", ex))
    }
  }

  @Transactional
  fun withdraw(money: Money): Result<String> {
    return try {
      val wallet = authService.getUserWallet()

      val updatedWallet = wallet.remove(money)

      transactionService.createTransaction(wallet, money, TransactionType.WITHDRAW)

      walletRepository.save(updatedWallet)

      Result.success("Added ${money.amount} ${money.currency} to wallet successfully")
    } catch (ex: IllegalArgumentException) {
      Result.failure(ex)
    } catch (ex: Exception) {
      Result.failure(RuntimeException("Failed to process deposit: ${ex.message}", ex))
    }
  }

  fun getTransactions(): List<TransactionDto> {
    val wallet = authService.getUserWallet()
    val transactions = wallet.transactions
    val transactionDtos = transactions.map { it.toDto() }
    return transactionDtos
  }

  @Transactional
  fun send(money: Money, to: P2PUser): Result<String> {
    return try {
      val senderWallet = authService.getUserWallet()
      val receiverWallet = walletRepository.findByEmailOrAlias(to.email, to.alias)
        ?: return Result.failure(IllegalArgumentException("Receiver wallet not found."))
      val updatedSenderWallet = senderWallet.remove(money)
      val updatedReceiverWallet = receiverWallet.add(money)

      transactionService.createTransaction(senderWallet, money, TransactionType.P2P, receiverWallet)

      walletRepository.save(updatedSenderWallet)
      walletRepository.save(updatedReceiverWallet)

      Result.success("Sent ${money.amount} ${money.currency} successfully")
    } catch (ex: IllegalArgumentException) {
      Result.failure(ex)
    } catch (ex: Exception) {
      Result.failure(RuntimeException("Failed to process deposit: ${ex.message}", ex))
    }
  }
}
