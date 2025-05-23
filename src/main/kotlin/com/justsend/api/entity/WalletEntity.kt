package com.justsend.api.entity

import jakarta.persistence.CollectionTable
import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.MapKeyColumn
import jakarta.persistence.Table
import org.hibernate.annotations.UuidGenerator
import java.util.UUID

@Entity
@Table(name = "wallets")
class WalletEntity(

  @Id
  @UuidGenerator
  @Column
  val id: UUID? = null,

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(
    name = "wallet_balances",
    joinColumns = [JoinColumn(name = "wallet_id")]
  )
  @MapKeyColumn(name = "currency")
  @Column(name = "amount")
  var balances: MutableMap<String, Double>
) {
  constructor() : this(null, mutableMapOf())
}
