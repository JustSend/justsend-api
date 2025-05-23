package com.justsend.api.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
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

  @Column(name = "wallet_id", nullable = false, unique = true)
  val walletId: UUID
) {
  constructor() : this(null, "", "", UUID.randomUUID())
}
