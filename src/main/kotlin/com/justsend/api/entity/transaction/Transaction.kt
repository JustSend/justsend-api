package com.justsend.api.entity.transaction

import com.justsend.api.dto.Amount
import com.justsend.api.dto.Currency
import com.justsend.api.dto.TransactionType
import com.justsend.api.entity.Wallet
import jakarta.persistence.Column
import jakarta.persistence.DiscriminatorColumn
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Inheritance
import jakarta.persistence.InheritanceType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "transactions")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "transaction_kind")
abstract class Transaction(

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(updatable = false, nullable = false)
  open val id: UUID? = null,

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "wallet_id", nullable = false)
  open val wallet: Wallet,

  @Column(nullable = false)
  open val amount: Amount,

  @Column(nullable = false)
  open val currency: Currency,

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  open val type: TransactionType,

  @Column(nullable = false)
  open val timestamp: Instant = Instant.now()
)
