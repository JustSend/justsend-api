package com.justsend.api.service

import com.justsend.api.dto.Money
import com.justsend.api.dto.TransactionType
import com.justsend.api.entity.Wallet
import com.justsend.api.entity.transaction.DepositTransaction
import com.justsend.api.entity.transaction.Transaction
import com.justsend.api.entity.transaction.WithdrawTransaction
import com.justsend.api.repository.TransactionRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TransactionServiceTest {
  private lateinit var transactionRepository: TransactionRepository
  private lateinit var authService: AuthService
  private lateinit var transactionService: TransactionService

  @BeforeEach
  fun setUp() {
    transactionRepository = mockk(relaxed = true)
    authService = mockk(relaxed = true)
    transactionService = TransactionService(transactionRepository)
  }

  @Test
  fun `createTransaction creates and saves DepositTransaction`() {
    val wallet = Wallet("wallet1")
    val money = Money("USD", 100.0)
    val slot = slot<Transaction>()
    every { transactionRepository.save(capture(slot)) } answers { slot.captured }

    val result = transactionService.createTransaction(wallet, money, TransactionType.DEPOSIT)

    assertTrue(result is DepositTransaction)
    assertEquals(wallet, result.wallet)
    assertEquals(100.0, result.amount)
    assertEquals("USD", result.currency)
    verify { transactionRepository.save(any()) }
  }

  @Test
  fun `createTransaction creates and saves WithdrawTransaction`() {
    val wallet = Wallet("wallet2")
    val money = Money("EUR", 50.0)
    val slot = slot<Transaction>()
    every { transactionRepository.save(capture(slot)) } answers { slot.captured }

    val result = transactionService.createTransaction(wallet, money, TransactionType.WITHDRAW)

    assertTrue(result is WithdrawTransaction)
    assertEquals(wallet, result.wallet)
    assertEquals(50.0, result.amount)
    assertEquals("EUR", result.currency)
    verify { transactionRepository.save(any()) }
  }
}
