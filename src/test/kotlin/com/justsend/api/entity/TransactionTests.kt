package com.justsend.api.entity

import com.justsend.api.dto.TransactionType
import com.justsend.api.entity.transaction.DepositTransaction
import com.justsend.api.entity.transaction.ReceiveTransaction
import com.justsend.api.entity.transaction.SendTransaction
import com.justsend.api.entity.transaction.WithdrawTransaction
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.Instant

class TransactionTests {
  @Test
  fun `DepositTransaction should have correct values`() {
    val wallet = Wallet("wallet1")
    val deposit = DepositTransaction(wallet, 100.0, "USD")
    Assertions.assertEquals(TransactionType.DEPOSIT, deposit.type)
    Assertions.assertEquals(wallet, deposit.wallet)
    Assertions.assertEquals(100.0, deposit.amount)
    Assertions.assertEquals("USD", deposit.currency)
  }

  @Test
  fun `WithdrawTransaction should have correct values`() {
    val wallet = Wallet("wallet2")
    val withdraw = WithdrawTransaction(wallet, 50.0, "EUR")
    Assertions.assertEquals(TransactionType.WITHDRAW, withdraw.type)
    Assertions.assertEquals(wallet, withdraw.wallet)
    Assertions.assertEquals(50.0, withdraw.amount)
    Assertions.assertEquals("EUR", withdraw.currency)
  }

  @Test
  fun `DepositTransaction should store and return the correct timestamp`() {
    val wallet = Wallet("wallet3")
    val deposit = DepositTransaction(wallet, 200.0, "GBP")
    Assertions.assertNotNull(deposit.timestamp)
    val now = deposit.timestamp
    val deposit2 = DepositTransaction(wallet, 300.0, "GBP", now)
    Assertions.assertEquals(now, deposit2.timestamp)
  }

  @Test
  fun `WithdrawTransaction should store and return the correct timestamp`() {
    val wallet = Wallet("wallet4")
    val withdraw = WithdrawTransaction(wallet, 75.0, "JPY")
    Assertions.assertNotNull(withdraw.timestamp)
    val now = withdraw.timestamp
    val withdraw2 = WithdrawTransaction(wallet, 80.0, "JPY", now)
    Assertions.assertEquals(now, withdraw2.timestamp)
  }

  @Test
  fun `DepositTransaction should allow zero amount`() {
    val wallet = Wallet("wallet5")
    val deposit = DepositTransaction(wallet, 0.0, "USD")
    Assertions.assertEquals(0.0, deposit.amount)
  }

  @Test
  fun `WithdrawTransaction should allow zero amount`() {
    val wallet = Wallet("wallet6")
    val withdraw = WithdrawTransaction(wallet, 0.0, "USD")
    Assertions.assertEquals(0.0, withdraw.amount)
  }

  @Test
  fun `DepositTransaction should allow negative amount if business logic allows`() {
    val wallet = Wallet("wallet7")
    val deposit = DepositTransaction(wallet, -10.0, "USD")
    Assertions.assertEquals(-10.0, deposit.amount)
  }

  @Test
  fun `WithdrawTransaction should allow negative amount if business logic allows`() {
    val wallet = Wallet("wallet8")
    val withdraw = WithdrawTransaction(wallet, -20.0, "USD")
    Assertions.assertEquals(-20.0, withdraw.amount)
  }

  @Test
  fun `SendTransaction should have correct values`() {
    val sender = Wallet("sender1")
    val recipient = Wallet("recipient1")
    val send = SendTransaction(sender, 42.0, "USD", recipientWallet = recipient)
    Assertions.assertEquals(TransactionType.SEND, send.type)
    Assertions.assertEquals(sender, send.wallet)
    Assertions.assertEquals(42.0, send.amount)
    Assertions.assertEquals("USD", send.currency)
    Assertions.assertEquals(recipient, send.recipientWallet)
  }

  @Test
  fun `ReceiveTransaction should have correct values`() {
    val receiver = Wallet("receiver1")
    val sender = Wallet("sender2")
    val receive = ReceiveTransaction(receiver, 55.0, "EUR", senderWallet = sender)
    Assertions.assertEquals(TransactionType.RECEIVE, receive.type)
    Assertions.assertEquals(receiver, receive.wallet)
    Assertions.assertEquals(55.0, receive.amount)
    Assertions.assertEquals("EUR", receive.currency)
    Assertions.assertEquals(sender, receive.senderWallet)
  }

  @Test
  fun `SendTransaction should store and return the correct timestamp`() {
    val sender = Wallet("sender3")
    val recipient = Wallet("recipient3")
    val now = Instant.now()
    val send = SendTransaction(sender, 10.0, "GBP", now, recipient)
    Assertions.assertEquals(now, send.timestamp)
  }

  @Test
  fun `ReceiveTransaction should store and return the correct timestamp`() {
    val receiver = Wallet("receiver3")
    val sender = Wallet("sender4")
    val now = Instant.now()
    val receive = ReceiveTransaction(receiver, 20.0, "JPY", now, sender)
    Assertions.assertEquals(now, receive.timestamp)
  }

  @Test
  fun `SendTransaction should allow zero amount`() {
    val sender = Wallet("sender4")
    val recipient = Wallet("recipient4")
    val send = SendTransaction(sender, 0.0, "USD", recipientWallet = recipient)
    Assertions.assertEquals(0.0, send.amount)
  }

  @Test
  fun `ReceiveTransaction should allow zero amount`() {
    val receiver = Wallet("receiver4")
    val sender = Wallet("sender5")
    val receive = ReceiveTransaction(receiver, 0.0, "USD", senderWallet = sender)
    Assertions.assertEquals(0.0, receive.amount)
  }

  @Test
  fun `SendTransaction should allow negative amount if business logic allows`() {
    val sender = Wallet("sender5")
    val recipient = Wallet("recipient5")
    val send = SendTransaction(sender, -10.0, "USD", recipientWallet = recipient)
    Assertions.assertEquals(-10.0, send.amount)
  }

  @Test
  fun `ReceiveTransaction should allow negative amount if business logic allows`() {
    val receiver = Wallet("receiver5")
    val sender = Wallet("sender6")
    val receive = ReceiveTransaction(receiver, -20.0, "USD", senderWallet = sender)
    Assertions.assertEquals(-20.0, receive.amount)
  }
}
