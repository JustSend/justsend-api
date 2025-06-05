package com.justsend.api.entity.transaction

import com.justsend.api.dto.Amount
import com.justsend.api.dto.Currency
import com.justsend.api.dto.TransactionType
import com.justsend.api.entity.Wallet
import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity
import java.time.Instant

@Entity
@DiscriminatorValue("WITHDRAW")
class WithdrawTransaction(
  wallet: Wallet,
  amount: Amount,
  currency: Currency,
  timestamp: Instant = Instant.now()
) : Transaction(
  wallet = wallet,
  amount = amount,
  currency = currency,
  type = TransactionType.WITHDRAW,
  timestamp = timestamp
)
