package com.justsend.api.dto

import com.justsend.api.entity.transaction.P2PTransaction
import com.justsend.api.entity.transaction.Transaction
import java.time.Instant
import java.util.UUID

data class TransactionDto(
  val id: UUID,
  val walletId: String,
  val amount: Amount,
  val currency: Currency,
  val type: TransactionType,
  val createdAt: Instant,
  val email: String? = null
)

fun Transaction.toDto(): TransactionDto {
  return when (this) {
    is P2PTransaction -> TransactionDto(
      id = this.id!!,
      walletId = this.wallet.id,
      amount = this.amount,
      currency = this.currency,
      type = this.type,
      createdAt = this.timestamp,
      email = this.wallet.email
    )
    else -> TransactionDto(
      id = this.id!!,
      walletId = this.wallet.id,
      amount = this.amount,
      currency = this.currency,
      type = this.type,
      createdAt = this.timestamp
    )
  }
}
