package com.justsend.api.entity

import com.justsend.api.model.Money
import com.justsend.api.model.Wallet
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import java.util.UUID

@Entity
data class WalletEntity(
  @Id val id: String = UUID.randomUUID().toString(),
  @ElementCollection(fetch = FetchType.EAGER)
  val balances: Map<String, Double>
) {
  fun toWallet(): Wallet {
    val wallet = Wallet()
    balances.forEach { (currency, amount) ->
      wallet.add(Money(currency, amount))
    }
    return wallet
  }

  companion object {
    fun from(wallet: Wallet, id: String): WalletEntity {
      return WalletEntity(id, wallet.getAllBalances())
    }
  }
}
