package com.justsend.api

import com.justsend.api.dto.TransactionType
import com.justsend.api.entity.Transaction
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Profile
import kotlin.test.assertEquals

@SpringBootTest
@Profile("test")
class TransactionTests {

  @Test
  fun `transaction entity should be initialized with ARS and no money`() {
    val transaction = Transaction()

    assertEquals(0.0, transaction.amount)
    assertEquals("ARS", transaction.currency)
    assertEquals(TransactionType.INIT, transaction.type)
  }
}
