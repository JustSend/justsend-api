package com.justsend.api.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

@Entity
@Table(name = "users")
class User(

  @Id
  @Column
  val id: String,

  @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "wallet_id", referencedColumnName = "id", nullable = false)
  var wallet: WalletEntity
) {
  constructor() : this("", WalletEntity())
}
