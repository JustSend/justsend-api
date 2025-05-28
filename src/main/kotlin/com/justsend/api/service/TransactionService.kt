package com.justsend.api.service

import com.justsend.api.dto.Money
import com.justsend.api.dto.TransactionType
import com.justsend.api.entity.TransactionEntity
import com.justsend.api.entity.WalletEntity
import com.justsend.api.repository.TransactionRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class TransactionService(
  private val transactionRepository: TransactionRepository
) {
  fun createTransaction(
    walletEntity: WalletEntity,
    money: Money,
    transactionType: TransactionType
  ): TransactionEntity =
    transactionRepository.save(
      TransactionEntity(
        UUID.randomUUID(),
        walletEntity,
        money.amount,
        money.currency,
        transactionType
      )
    )
}
