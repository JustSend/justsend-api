package com.justsend.api.domain

import com.justsend.api.dto.Amount
import com.justsend.api.dto.Currency
import com.justsend.api.dto.Money
import com.justsend.api.dto.TransactionType
import java.util.UUID

class Wallet(
  private val balances: Map<Currency, Amount>,
  private val transactions: List<Transaction>
) {

  constructor() : this(emptyMap(), emptyList())

  fun add(money: Money): Wallet {
    require(money.amount >= 0.0) { "Amount cannot be negative" }
    val newBalances = balances.toMutableMap()
    val updatedAmount = newBalances.getOrDefault(money.currency, 0.0) + money.amount
    newBalances[money.currency] = updatedAmount
    val newTransactions = transactions.toMutableList()
    newTransactions.add(
      Transaction(UUID.randomUUID(), this, money.amount, money.currency, TransactionType.DEPOSIT)
    )

    return Wallet(newBalances, newTransactions)
  }

  fun remove(money: Money): Wallet {
    require(money.amount >= 0.0) { "Amount cannot be negative" }
    val currentAmount = balances.getOrDefault(money.currency, 0.0)
    require(currentAmount >= money.amount) {
      "Insufficient balance: trying to remove ${money.amount} from $currentAmount ${money.currency}"
    }
    val newBalances = balances.toMutableMap()
    val newTransactions = transactions.toMutableList()
    val updatedAmount = currentAmount - money.amount
    newBalances[money.currency] = updatedAmount
    newTransactions.add(
      Transaction(UUID.randomUUID(), this, money.amount, money.currency, TransactionType.EXTRACTION)
    )

    return Wallet(newBalances, newTransactions)
  }

  fun getBalanceIn(currency: Currency): Amount = balances.getOrDefault(currency, 0.0)
  fun getAllBalances(): Map<Currency, Amount> = balances.toMap()
}
