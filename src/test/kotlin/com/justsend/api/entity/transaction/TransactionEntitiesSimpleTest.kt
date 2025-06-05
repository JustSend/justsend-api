package com.justsend.api.entity.transaction

import com.justsend.api.dto.TransactionType
import com.justsend.api.entity.Wallet
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class TransactionEntitiesSimpleTest {
  @Test
  fun `DepositTransaction should have correct values`() {
    val wallet = Wallet("wallet1")
    val deposit = DepositTransaction(wallet, 100.0, "USD")
    assertEquals(TransactionType.DEPOSIT, deposit.type)
    assertEquals(wallet, deposit.wallet)
    assertEquals(100.0, deposit.amount)
    assertEquals("USD", deposit.currency)
  }

  @Test
  fun `WithdrawTransaction should have correct values`() {
    val wallet = Wallet("wallet2")
    val withdraw = WithdrawTransaction(wallet, 50.0, "EUR")
    assertEquals(TransactionType.WITHDRAW, withdraw.type)
    assertEquals(wallet, withdraw.wallet)
    assertEquals(50.0, withdraw.amount)
    assertEquals("EUR", withdraw.currency)
  }

  @Test
  fun `DepositTransaction should store and return the correct timestamp`() {
    val wallet = Wallet("wallet3")
    val deposit = DepositTransaction(wallet, 200.0, "GBP")
    assertNotNull(deposit.timestamp)
    val now = deposit.timestamp
    val deposit2 = DepositTransaction(wallet, 300.0, "GBP", now)
    assertEquals(now, deposit2.timestamp)
  }

  @Test
  fun `WithdrawTransaction should store and return the correct timestamp`() {
    val wallet = Wallet("wallet4")
    val withdraw = WithdrawTransaction(wallet, 75.0, "JPY")
    assertNotNull(withdraw.timestamp)
    val now = withdraw.timestamp
    val withdraw2 = WithdrawTransaction(wallet, 80.0, "JPY", now)
    assertEquals(now, withdraw2.timestamp)
  }

  @Test
  fun `DepositTransaction should allow zero amount`() {
    val wallet = Wallet("wallet5")
    val deposit = DepositTransaction(wallet, 0.0, "USD")
    assertEquals(0.0, deposit.amount)
  }

  @Test
  fun `WithdrawTransaction should allow zero amount`() {
    val wallet = Wallet("wallet6")
    val withdraw = WithdrawTransaction(wallet, 0.0, "USD")
    assertEquals(0.0, withdraw.amount)
  }

  @Test
  fun `DepositTransaction should allow negative amount if business logic allows`() {
    val wallet = Wallet("wallet7")
    val deposit = DepositTransaction(wallet, -10.0, "USD")
    assertEquals(-10.0, deposit.amount)
  }

  @Test
  fun `WithdrawTransaction should allow negative amount if business logic allows`() {
    val wallet = Wallet("wallet8")
    val withdraw = WithdrawTransaction(wallet, -20.0, "USD")
    assertEquals(-20.0, withdraw.amount)
  }
}
