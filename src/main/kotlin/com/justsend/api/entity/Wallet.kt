package com.justsend.api.entity

import com.justsend.api.dto.Amount
import com.justsend.api.dto.Currency
import com.justsend.api.dto.Money
import com.justsend.api.entity.transaction.Transaction
import com.justsend.api.util.generateAlias
import jakarta.persistence.CascadeType
import jakarta.persistence.CollectionTable
import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.MapKeyColumn
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "wallets")
class Wallet(

  @Id
  @Column(name = "id")
  val id: String = "",

  @Column(name = "alias")
  val alias: String = generateAlias(),

  @Column(name = "email")
  val email: String = "",

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(
    name = "wallet_balances",
    joinColumns = [JoinColumn(name = "wallet_id")]
  )
  @MapKeyColumn(name = "currency")
  @Column(name = "amount")
  var balances: MutableMap<String, Double> = mutableMapOf(),

  @OneToMany(mappedBy = "wallet", cascade = [CascadeType.ALL], fetch = FetchType.EAGER, orphanRemoval = true)
  val transactions: MutableList<Transaction> = mutableListOf()
) {

  fun add(money: Money): Wallet {
    require(money.amount >= 0.0) { "Amount cannot be negative" }
    val newBalances = balances.toMutableMap()
    val updatedAmount = newBalances.getOrDefault(money.currency, 0.0) + money.amount
    newBalances[money.currency] = updatedAmount
    return Wallet(id, alias, email, newBalances, transactions)
  }

  fun remove(money: Money): Wallet {
    require(money.amount >= 0.0) { "Amount cannot be negative" }
    val currentAmount = balances.getOrDefault(money.currency, 0.0)
    require(currentAmount >= money.amount) {
      "Insufficient balance: trying to remove ${money.amount} from $currentAmount ${money.currency}"
    }
    val newBalances = balances.toMutableMap()
    val updatedAmount = currentAmount - money.amount
    newBalances[money.currency] = updatedAmount

    return Wallet(id, alias, email, newBalances, transactions)
  }

  fun getBalanceIn(currency: Currency): Amount = balances.getOrDefault(currency, 0.0)
  fun getAllBalances(): Map<Currency, Amount> = balances.toMap()
}
