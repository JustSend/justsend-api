package com.justsend.api.entity

import com.justsend.api.dto.Amount
import com.justsend.api.dto.Currency
import com.justsend.api.dto.TransactionType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "transactions")
class TransactionEntity(
  @Id
  @GeneratedValue
  val id: UUID? = null,

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "wallet_id", nullable = false)
  val wallet: WalletEntity,

  @Column(nullable = false)
  val amount: Amount,

  @Column(nullable = false)
  val currency: Currency,

  @Column(nullable = false)
  val type: TransactionType,

  @Column(nullable = false, insertable = false)
  val timestamp: Instant = Instant.now()
) {
  constructor() : this(
    UUID.randomUUID(),
    WalletEntity(),
    0.0,
    "ARS",
    TransactionType.INIT
  )
}
