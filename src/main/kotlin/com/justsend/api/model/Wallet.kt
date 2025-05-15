package com.justsend.api.model

class Wallet private constructor(
  private val balances: Map<Currency, Amount>
) {

  constructor() : this(emptyMap())

  fun add(money: Money): Wallet {
    require(money.amount >= 0.0) { "Amount cannot be negative" }
    val newBalances = balances.toMutableMap()
    val updatedAmount = newBalances.getOrDefault(money.currency, 0.0) + money.amount
    newBalances[money.currency] = updatedAmount
    return Wallet(newBalances)
  }

  fun remove(money: Money): Wallet {
    require(money.amount >= 0.0) { "Amount cannot be negative" }
    val currentAmount = balances.getOrDefault(money.currency, 0.0)
    require(currentAmount >= money.amount) {
      "Insufficient balance: trying to remove ${money.amount} from $currentAmount ${money.currency}"
    }
    val newBalances = balances.toMutableMap()
    val updatedAmount = currentAmount - money.amount
    if (updatedAmount == 0.0) {
      newBalances.remove(money.currency)
    } else {
      newBalances[money.currency] = updatedAmount
    }
    return Wallet(newBalances)
  }

  fun getBalanceIn(currency: Currency): Amount = balances.getOrDefault(currency, 0.0)
  fun getAllBalances(): Map<Currency, Amount> = balances.toMap()
}
