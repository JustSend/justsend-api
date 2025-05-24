package com.justsend.api.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import org.hibernate.annotations.UuidGenerator
import java.util.UUID

@Entity
@Table(name = "users")
class User(

  @Id
  @UuidGenerator
  @Column
  val id: UUID? = null,

  @Column(nullable = false, unique = true)
  val email: String,

  @Column(nullable = false)
  val password: String,

  @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "wallet_id", referencedColumnName = "id", nullable = false)
  var wallet: WalletEntity
) {
  constructor() : this(null, "", "", WalletEntity())
}
