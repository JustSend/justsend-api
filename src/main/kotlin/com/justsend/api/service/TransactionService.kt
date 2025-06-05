package com.justsend.api.service

import com.justsend.api.dto.Money
import com.justsend.api.dto.TransactionType
import com.justsend.api.entity.Wallet
import com.justsend.api.entity.transaction.DepositTransaction
import com.justsend.api.entity.transaction.P2PTransaction
import com.justsend.api.entity.transaction.Transaction
import com.justsend.api.entity.transaction.WithdrawTransaction
import com.justsend.api.repository.TransactionRepository
import org.springframework.stereotype.Service

@Service
class TransactionService(
  private val transactionRepository: TransactionRepository,
  private val authService: AuthService
) {
  fun createTransaction(
    wallet: Wallet,
    money: Money,
    transactionType: TransactionType,
    recipientWallet: Wallet? = null
  ): Transaction {
    val transaction = buildTransaction(wallet, money, transactionType, recipientWallet)
    return transactionRepository.save(transaction)
  }

  fun getAllTransactions(userWallet: Wallet?): List<Transaction> {
    val wallet = userWallet ?: authService.getUserWallet()
    return transactionRepository.findAllByWallet_Id(wallet.id)
  }

  private fun buildTransaction(
    wallet: Wallet,
    money: Money,
    transactionType: TransactionType,
    recipientWallet: Wallet? = null
  ): Transaction {
    return when (transactionType) {
      TransactionType.P2P -> {
        requireNotNull(recipientWallet) { "Recipient wallet is required for P2P transactions" }
        P2PTransaction(
          wallet = wallet,
          amount = money.amount,
          currency = money.currency,
          recipientWallet = recipientWallet
        )
      }
      TransactionType.DEPOSIT ->
        DepositTransaction(wallet, money.amount, money.currency)
      TransactionType.WITHDRAW ->
        WithdrawTransaction(wallet, money.amount, money.currency)
      else -> throw IllegalArgumentException("Unsupported transaction type")
    }
  }
}
