package com.justsend.api.domain

import com.justsend.api.dto.Amount
import com.justsend.api.dto.Currency
import com.justsend.api.dto.TransactionType
import java.time.Instant
import java.util.UUID

data class Transaction(
  val id: UUID? = null,
  val wallet: Wallet,
  val amount: Amount,
  val currency: Currency,
  val type: TransactionType,
  val timestamp: Instant = Instant.now()
)
