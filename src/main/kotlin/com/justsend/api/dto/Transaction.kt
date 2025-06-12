package com.justsend.api.dto

import com.justsend.api.entity.transaction.ReceiveTransaction
import com.justsend.api.entity.transaction.SendTransaction
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
  val email = when (this) {
    is ReceiveTransaction -> this.senderWallet.email
    is SendTransaction -> this.recipientWallet.email
    else -> null
  }

  return TransactionDto(
    id = this.id!!,
    walletId = this.wallet.id,
    amount = this.amount,
    currency = this.currency,
    type = this.type,
    createdAt = this.timestamp,
    email = email
  )
}
