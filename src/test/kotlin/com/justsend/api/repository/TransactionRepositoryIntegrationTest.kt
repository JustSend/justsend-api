package com.justsend.api.repository

import com.justsend.api.dto.TransactionType
import com.justsend.api.entity.Wallet
import com.justsend.api.entity.transaction.DepositTransaction
import com.justsend.api.entity.transaction.SendTransaction
import com.justsend.api.entity.transaction.WithdrawTransaction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class TransactionRepositoryIntegrationTest @Autowired constructor(
  val transactionRepository: TransactionRepository,
  val walletRepository: WalletRepository
) {
  @Test
  fun `should save and retrieve transaction`() {
    val wallet = Wallet(
      id = "test-wallet-id",
      email = "transaction@example.com",
      alias = "transactionalias",
      balances = mutableMapOf("USD" to 200.0)
    )
    val persistedWallet = walletRepository.save(wallet)

    val transaction = DepositTransaction(
      wallet = persistedWallet,
      amount = 50.0,
      currency = "USD"
    )
    val savedTransaction = transactionRepository.save(transaction)
    val found = transactionRepository.findById(savedTransaction.id!!).get()
    assertEquals(transaction.amount, found.amount)
    assertEquals(transaction.currency, found.currency)
    assertEquals(TransactionType.DEPOSIT, found.type)
    assertEquals(wallet.id, found.wallet.id)
  }

  @Test
  fun `should save and retrieve multiple transactions for the same wallet`() {
    val wallet = Wallet(
      id = "multi-wallet-id",
      email = "multi@example.com",
      alias = "multialias",
      balances = mutableMapOf("USD" to 500.0)
    )
    val persistedWallet = walletRepository.save(wallet)
    walletRepository.flush()

    val tx1 = DepositTransaction(wallet = persistedWallet, amount = 100.0, currency = "USD")
    val tx2 = WithdrawTransaction(wallet = persistedWallet, amount = 50.0, currency = "USD")
    transactionRepository.save(tx1)
    transactionRepository.save(tx2)

    val all = transactionRepository.findAll()
    val foundTx1 = all.find { it.amount == 100.0 }
    val foundTx2 = all.find { it.amount == 50.0 }
    assertEquals("USD", foundTx1?.currency)
    assertEquals("USD", foundTx2?.currency)
    assertEquals(persistedWallet.id, foundTx1?.wallet?.id)
    assertEquals(persistedWallet.id, foundTx2?.wallet?.id)
  }

  @Test
  fun `should save and retrieve SendTransaction with recipient`() {
    val sender = Wallet(
      id = "sender-wallet-id",
      email = "sender@example.com",
      alias = "senderalias",
      balances = mutableMapOf("USD" to 300.0)
    )
    val recipient = Wallet(
      id = "recipient-wallet-id",
      email = "recipient@example.com",
      alias = "recipientalias",
      balances = mutableMapOf("USD" to 100.0)
    )
    val persistedSender = walletRepository.save(sender)
    val persistedRecipient = walletRepository.save(recipient)
    walletRepository.flush()

    val sendTx = SendTransaction(
      wallet = persistedSender,
      amount = 25.0,
      currency = "USD",
      recipientWallet = persistedRecipient
    )
    val savedSendTx = transactionRepository.save(sendTx)
    val found = transactionRepository.findById(savedSendTx.id!!).get() as SendTransaction
    assertEquals(25.0, found.amount)
    assertEquals("USD", found.currency)
    assertEquals(persistedSender.id, found.wallet.id)
    assertEquals(persistedRecipient.id, found.recipientWallet.id)
  }
}
