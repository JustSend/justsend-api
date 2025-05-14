package com.justsend.api

import com.justsend.api.model.Money
import com.justsend.api.model.Wallet
import org.junit.jupiter.api.Assertions.assertThrows
import kotlin.test.Test

class WalletTests {

  val wallet = Wallet()
  val usd = "USD"
  val ars = "ARS"
  val tenDollars = Money(usd, 10.00)
  val tenPesos = Money(ars, 10.00)

  @Test
  fun `new wallet should start with no balance - 01`() {
    assert(wallet.getBalanceFor(usd) == 0.0)
    assert(wallet.getBalanceFor(ars) == 0.0)
  }

  @Test
  fun `adding positive balance to the wallet the wallet balance should not be cero - 02`() {
    wallet.add(tenDollars)
    val balanceInDollars = wallet.getBalanceFor(usd)
    assert(balanceInDollars != 0.0)
  }

  @Test
  fun `adding positive balance to wallet the wallet should have the new balance - 03`() {
    wallet.add(tenDollars)
    val balanceInDollars = wallet.getBalanceFor(usd)
    assert(balanceInDollars == 10.0)
  }

  @Test
  fun `adding negative money to wallet should throw exception - 04`() {
    val negativeMoney = Money(usd, -10.00)
    assertThrows(IllegalArgumentException::class.java) {
      wallet.add(negativeMoney)
    }
  }

  @Test
  fun `removing no money the wallet should be the same - 05`() {
    val noMoney = Money(usd, 0.0)
    wallet.add(tenDollars)
    wallet.remove(noMoney)
    val balanceInDollars = wallet.getBalanceFor(usd)
    assert(balanceInDollars == 10.0)
  }

  @Test
  fun `removing negative balance throw exception - 06`() {
    val negativeMoney = Money(usd, -10.00)
    assertThrows(IllegalArgumentException::class.java) {
      wallet.remove(negativeMoney)
    }
  }

  @Test
  fun `removing same balance as current balance should leave wallet with no balance - 07`() {
    wallet.add(tenDollars)
    wallet.remove(tenDollars)
    assert(wallet.getBalanceFor(usd) == 0.0)
  }

  @Test
  fun `removing more balance than actual should throw error - 08`() {
    wallet.add(tenDollars)
    assertThrows(IllegalArgumentException::class.java) {
      wallet.remove(Money(usd, 20.00))
    }
  }

  @Test
  fun `adding a currency and asking for another one should return correctly`() {
    wallet.add(tenDollars)
    assert(wallet.getBalanceFor(ars) == 0.0)
  }

  @Test
  fun `adding two currencies and asking for both should return correctly`() {
    wallet.add(tenDollars)
    wallet.add(tenPesos)
    assert(wallet.getBalanceFor(ars) == 10.0)
    assert(wallet.getBalanceFor(usd) == 10.0)
  }

  @Test
  fun `getting all balances for available currencies should return correctly`() {
    wallet.add(tenDollars)
    wallet.add(tenPesos)
    val balances = wallet.getAllBalances()
    assert(balances.containsKey(usd))
    assert(balances.containsKey(ars))
    assert(balances[usd] == 10.0)
    assert(balances[ars] == 10.0)
  }

  @Test
  fun `getting all balances for no currencies should return empty map`() {
    val balances = wallet.getAllBalances()
    assert(balances.isEmpty())
  }

  @Test
  fun `getting all balances for one currency should return map with one entry`() {
    wallet.add(tenDollars)
    val balances = wallet.getAllBalances()
    assert(balances.size == 1)
    assert(balances.containsKey(usd))
    assert(!balances.containsKey(ars))
    assert(balances[usd] == 10.0)
  }
}
