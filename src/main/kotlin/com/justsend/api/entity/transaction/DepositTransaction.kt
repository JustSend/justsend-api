package com.justsend.api.entity.transaction

import com.justsend.api.dto.Amount
import com.justsend.api.dto.Currency
import com.justsend.api.dto.TransactionType
import com.justsend.api.entity.Wallet
import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity
import java.time.Instant

@Entity
@DiscriminatorValue("DEPOSIT")
class DepositTransaction(
  wallet: Wallet = Wallet(),
  amount: Amount = 0.0,
  currency: Currency = "",
  timestamp: Instant = Instant.now()
) : Transaction(
  wallet = wallet,
  amount = amount,
  currency = currency,
  type = TransactionType.DEPOSIT,
  timestamp = timestamp
)
