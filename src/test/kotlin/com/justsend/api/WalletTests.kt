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
  fun `new wallet should start with no balance - 001`() {
    assert(wallet.getBalanceFor(usd) == 0.0)
    assert(wallet.getBalanceFor(ars) == 0.0)
  }

  @Test
  fun `adding positive balance to the wallet the wallet balance should not be cero - 002`() {
    wallet.add(tenDollars)
    val balanceInDollars = wallet.getBalanceFor(usd)
    assert(balanceInDollars != 0.0)
  }

  @Test
  fun `adding positive balance to wallet the wallet should have the new balance - 003`() {
    wallet.add(tenDollars)
    val balanceInDollars = wallet.getBalanceFor(usd)
    assert(balanceInDollars == 10.0)
  }

  @Test
  fun `adding negative money to wallet should throw exception - 004`() {
    val negativeMoney = Money(usd, -10.00)
    assertThrows(IllegalArgumentException::class.java) {
      wallet.add(negativeMoney)
    }
  }

  @Test
  fun `removing no money the wallet should be the same - 005`() {
    val noMoney = Money(usd, 0.0)
    wallet.add(tenDollars)
    wallet.remove(noMoney)
    val balanceInDollars = wallet.getBalanceFor(usd)
    assert(balanceInDollars == 10.0)
  }

  @Test
  fun `removing negative balance throw exception - 006`() {
    val negativeMoney = Money(usd, -10.00)
    assertThrows(IllegalArgumentException::class.java) {
      wallet.remove(negativeMoney)
    }
  }

  @Test
  fun `removing same balance as current balance should leave wallet with no balance - 007`() {
    wallet.add(tenDollars)
    wallet.remove(tenDollars)
    assert(wallet.getBalanceFor(usd) == 0.0)
  }

  @Test
  fun `removing more balance than actual should throw error - 008`() {
    wallet.add(tenDollars)
    assertThrows(IllegalArgumentException::class.java) {
      wallet.remove(Money(usd, 20.00))
    }
  }

  @Test
  fun `adding a currency and asking for another one should return correctly - 009`() {
    wallet.add(tenDollars)
    assert(wallet.getBalanceFor(ars) == 0.0)
  }

  @Test
  fun `adding two currencies and asking for both should return correctly - 010`() {
    wallet.add(tenDollars)
    wallet.add(tenPesos)
    assert(wallet.getBalanceFor(ars) == 10.0)
    assert(wallet.getBalanceFor(usd) == 10.0)
  }

  @Test
  fun `getAllBalances on new wallet should return empty map - 011`() {
    assert(wallet.getAllBalances().isEmpty())
  }

  @Test
  fun `adding to existing currency should accumulate balance - 012`() {
    wallet.add(tenDollars)
    wallet.add(Money(usd, 5.0))
    assert(wallet.getBalanceFor(usd) == 15.0)
  }

  @Test
  fun `removing partial balance should decrease balance correctly - 013`() {
    wallet.add(tenDollars)
    wallet.remove(Money(usd, 4.0))
    assert(wallet.getBalanceFor(usd) == 6.0)
  }

  @Test
  fun `removing all balance should not remove currency from balances map`() {
    wallet.add(tenDollars)
    wallet.remove(tenDollars)
    val balances = wallet.getAllBalances()
    assert(balances.containsKey(usd))
    assert(balances[usd] == 0.0)
  }
}
