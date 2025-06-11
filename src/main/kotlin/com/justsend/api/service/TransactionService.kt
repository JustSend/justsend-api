package com.justsend.api.service

import com.justsend.api.dto.Money
import com.justsend.api.dto.TransactionType
import com.justsend.api.entity.Wallet
import com.justsend.api.entity.transaction.DepositTransaction
import com.justsend.api.entity.transaction.ReceiveTransaction
import com.justsend.api.entity.transaction.SendTransaction
import com.justsend.api.entity.transaction.Transaction
import com.justsend.api.entity.transaction.WithdrawTransaction
import com.justsend.api.repository.TransactionRepository
import org.springframework.stereotype.Service

@Service
class TransactionService(
  private val transactionRepository: TransactionRepository
) {
  fun createTransaction(
    wallet: Wallet,
    money: Money,
    transactionType: TransactionType,
    otherWallet: Wallet? = null
  ): Transaction {
    val transaction = buildTransaction(wallet, money, transactionType, otherWallet)
    return transactionRepository.save(transaction)
  }

  private fun buildTransaction(
    wallet: Wallet,
    money: Money,
    transactionType: TransactionType,
    otherWallet: Wallet? = null
  ): Transaction {
    return when (transactionType) {
      TransactionType.DEPOSIT ->
        DepositTransaction(wallet, money.amount, money.currency)
      TransactionType.WITHDRAW ->
        WithdrawTransaction(wallet, money.amount, money.currency)

      TransactionType.SEND -> {
        SendTransaction(
          wallet = wallet,
          amount = money.amount,
          currency = money.currency,
          recipientWallet = otherWallet!!
        )
      }
      TransactionType.RECEIVE -> {
        ReceiveTransaction(
          wallet = otherWallet!!,
          amount = money.amount,
          currency = money.currency,
          senderWallet = wallet
        )
      }
    }
  }
}
