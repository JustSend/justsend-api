package com.justsend.api.service

import com.justsend.api.dto.*
import com.justsend.api.entity.Wallet
import com.justsend.api.repository.WalletRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class WalletServiceTests {

  private lateinit var walletRepository: WalletRepository
  private lateinit var transactionService: TransactionService
  private lateinit var authService: AuthService
  private lateinit var walletService: WalletService
  private lateinit var wallet: Wallet

  @BeforeEach
  fun setup() {
    walletRepository = mockk(relaxed = true)
    transactionService = mockk(relaxed = true)
    authService = mockk(relaxed = true)
    walletService = WalletService(authService, transactionService, walletRepository)

    wallet = mockk(relaxed = true)
    every { authService.getUserWallet() } returns wallet
  }

  @Test
  fun `deposit should add money to wallet`() {
    val money = Money("USD", 100.0)
    every { wallet.add(money) } returns wallet
    every { walletRepository.save(any()) } returns wallet

    val result = walletService.deposit(money, wallet)

    assertTrue(result.isSuccess)
    verify { wallet.add(money) }
    verify { walletRepository.save(wallet) }
  }

  @Test
  fun `withdraw should remove money from wallet`() {
    val money = Money("USD", 50.0)
    every { wallet.remove(money) } returns wallet
    every { walletRepository.save(any()) } returns wallet

    val result = walletService.withdraw(money)

    assertTrue(result.isSuccess)
    verify { wallet.remove(money) }
    verify { walletRepository.save(wallet) }
  }

  @Test
  fun `send should transfer money to another user`() {
    val money = Money("USD", 100.0)
    val receiver = UserInfo(email = "test@receiver.com", alias = "receiver")
    val receiverWallet = mockk<Wallet>(relaxed = true)

    every { walletRepository.findByEmailOrAlias(receiver.email, receiver.alias) } returns receiverWallet
    every { wallet.remove(money) } returns wallet
    every { receiverWallet.add(money) } returns receiverWallet
    every { walletRepository.save(wallet) } returns wallet
    every { walletRepository.save(receiverWallet) } returns receiverWallet

    val result = walletService.send(money, receiver)

    assertTrue(result.isSuccess)
    verify { wallet.remove(money) }
    verify { receiverWallet.add(money) }
    verify { walletRepository.save(wallet) }
    verify { walletRepository.save(receiverWallet) }
  }

  @Test
  fun `getBalances should return wallet balances`() {
    val balances = mapOf("USD" to 100.0, "EUR" to 50.0)
    every { wallet.getAllBalances() } returns balances

    val result = walletService.getBalances()

    assertEquals(balances, result)
    verify { wallet.getAllBalances() }
  }

  @Test
  fun `deposit should fail and return failure result on exception`() {
    val money = Money("USD", 100.0)
    every { wallet.add(money) } throws IllegalArgumentException("Invalid deposit")

    val result = walletService.deposit(money, wallet)

    assertTrue(result.isFailure)
    assertTrue(result.exceptionOrNull() is IllegalArgumentException)
  }

  @Test
  fun `withdraw should fail and return failure result on exception`() {
    val money = Money("USD", 100.0)
    every { wallet.remove(money) } throws IllegalArgumentException("Insufficient funds")
    every { authService.getUserWallet() } returns wallet

    val result = walletService.withdraw(money)

    assertTrue(result.isFailure)
    assertTrue(result.exceptionOrNull() is IllegalArgumentException)
  }

  @Test
  fun `send should fail if receiver wallet not found`() {
    val money = Money("USD", 100.0)
    val receiver = UserInfo(email = "notfound@receiver.com", alias = "notfound")
    every { walletRepository.findByEmailOrAlias(receiver.email, receiver.alias) } returns null

    val result = walletService.send(money, receiver)

    assertTrue(result.isFailure)
    assertTrue(result.exceptionOrNull() is IllegalArgumentException)
  }
}
