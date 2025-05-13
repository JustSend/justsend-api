package com.justsend.api

import com.justsend.api.model.Wallet
import org.junit.jupiter.api.Assertions.assertThrows
import kotlin.test.Test

class WalletTests {

  val wallet = Wallet()

  @Test
  fun `new wallet should start with no balance - 01`() {
    assert(wallet.getBalance() == 0)
  }

  @Test
  fun `adding positive balance to the wallet the wallet balance should not be cero - 02`() {
    wallet.addBalance(10)
    assert(wallet.getBalance() != 0)
  }

  @Test
  fun `adding positive balance to wallet the wallet should have the new balance - 03`() {
    wallet.addBalance(10)
    assert(wallet.getBalance() != 0)
  }

  @Test
  fun `adding negative balance to wallet should throw exception - 04`() {
    assertThrows(IllegalArgumentException::class.java) {
      wallet.addBalance(-10)
    }
  }

  @Test
  fun `removing 0 balance should be the same - 05`() {
    wallet.addBalance(10)
    wallet.removeBalance(0)
    assert(wallet.getBalance() == 10)
  }

  @Test
  fun `removing negative balance throw exception - 06`() {
    assertThrows(IllegalArgumentException::class.java) {
      wallet.removeBalance(-10)
    }
  }

  @Test
  fun `removing same balance as current balance should leave wallet with no balance - 07`() {
    wallet.addBalance(10)
    wallet.removeBalance(10)
    assert(wallet.getBalance() == 0)
  }

  @Test
  fun `removing more balance than actual should throw error - 08`() {
    val wallet = Wallet()
    wallet.addBalance(10)
    assertThrows(IllegalArgumentException::class.java) {
      wallet.removeBalance(20)
    }
  }
}
