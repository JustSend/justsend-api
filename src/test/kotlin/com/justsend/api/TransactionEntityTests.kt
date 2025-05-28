package com.justsend.api

import com.justsend.api.dto.TransactionType
import com.justsend.api.entity.TransactionEntity
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class TransactionEntityTests {

  @Test
  fun `transaction entity should be initialized with ARS and no money`() {
    val transaction = TransactionEntity()

    assertEquals(0.0, transaction.amount)
    assertEquals("ARS", transaction.currency)
    assertEquals(TransactionType.INIT, transaction.type)
  }
}
