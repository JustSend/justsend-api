package com.justsend.api.service

import com.justsend.api.dto.Money
import com.justsend.api.dto.TransactionType
import com.justsend.api.entity.Transaction
import com.justsend.api.entity.Wallet
import com.justsend.api.repository.TransactionRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class TransactionService(
  private val transactionRepository: TransactionRepository,
  private val authService: AuthService
) {
  fun createTransaction(
    wallet: Wallet,
    money: Money,
    transactionType: TransactionType
  ): Transaction =
    transactionRepository.save(
      Transaction(
        UUID.randomUUID(),
        wallet,
        money.amount,
        money.currency,
        transactionType
      )
    )

  fun getAllTransactions(userWallet: Wallet?): List<Transaction> {
    val wallet = userWallet ?: authService.getUserWallet()
    return transactionRepository.findAllByWallet_Id(wallet.id)
  }
}
