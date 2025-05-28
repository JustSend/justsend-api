package com.justsend.api

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Profile

@SpringBootTest
@Profile("test")
class TransactionTests {

//  @Test
//  fun `transaction entity should be initialized with ARS and no money`() {
//    val transaction = Transaction()
//
//    assertEquals(0.0, transaction.amount)
//    assertEquals("ARS", transaction.currency)
//    assertEquals(TransactionType.INIT, transaction.type)
//  }
}
