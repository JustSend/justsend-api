package com.justsend.api.dto

import java.time.Instant
import java.util.UUID

data class TransactionDto(
  val id: UUID,
  val walletId: String,
  val amount: Amount,
  val currency: Currency,
  val type: TransactionType,
  val createdAt: Instant
)
