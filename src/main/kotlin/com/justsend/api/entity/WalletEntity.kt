package com.justsend.api.entity

import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import org.hibernate.annotations.UuidGenerator
import java.util.UUID

@Entity
class WalletEntity(

  @Id
  @UuidGenerator
  @Column
  val id: UUID? = null,

  @ElementCollection(fetch = FetchType.EAGER)
  val balances: Map<String, Double>
) {
  constructor() : this(null, emptyMap())
}
