package com.justsend.api.model

class Wallet() {

  private var balances = mutableMapOf<Currency, Amount>()

  fun add(money: Money) {
    require(money.amount >= 0.0) { "Amount cannot be negative" }
    balances[money.currency] = balances.getOrDefault(money.currency, 0.0) + money.amount
  }

  fun remove(money: Money) {
    require(money.amount >= 0.0) { "Amount cannot be negative" }
    val currentAmount = balances.getOrDefault(money.currency, 0.0)
    require(currentAmount >= money.amount) {
      "Insufficient balance: trying to remove ${money.amount} from $currentAmount ${money.currency}"
    }
    balances[money.currency] = currentAmount - money.amount
  }

  fun getBalanceIn(currency: Currency): Double = balances.getOrDefault(currency, 0.0)
  fun getAllBalances(): Map<Currency, Amount> = balances.toMap()
}
