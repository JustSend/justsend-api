package com.justsend.api.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "users")
data class User(

  @Id
  @GeneratedValue
  val id: UUID? = null,

  @Column(nullable = false, unique = true)
  val name: String,

  @Column(nullable = false)
  val password: String,

  @Column(name = "wallet_id", nullable = false, unique = true)
  val walletId: UUID
)
