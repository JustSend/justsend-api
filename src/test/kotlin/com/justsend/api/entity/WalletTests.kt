package com.justsend.api.entity

import com.justsend.api.dto.Money
import org.junit.jupiter.api.Assertions
import kotlin.math.abs
import kotlin.test.Test

class WalletTests {

  val wallet = Wallet()
  val usd = "USD"
  val ars = "ARS"
  val tenDollars = Money(usd, 10.00)
  val tenPesos = Money(ars, 10.00)

  @Test
  fun `new wallet should start with no balance - 01`() {
    val balances = wallet.getAllBalances()
    assert(balances.getOrDefault(usd, 0.0) == 0.0)
    assert(balances.getOrDefault(ars, 0.0) == 0.0)
  }

  @Test
  fun `adding positive balance to the wallet the wallet balance should not be cero - 02`() {
    val newWallet = wallet.add(tenDollars)
    val balanceInDollars = newWallet.getAllBalances()[usd]
    assert(balanceInDollars != 0.0)
  }

  @Test
  fun `adding positive balance to wallet the wallet should have the new balance - 03`() {
    val newWallet = wallet.add(tenDollars)
    val balanceInDollars = newWallet.getAllBalances()[usd]
    assert(balanceInDollars == 10.0)
  }

  @Test
  fun `adding negative money to wallet should throw exception - 04`() {
    val negativeMoney = Money(usd, -10.00)
    Assertions.assertThrows(IllegalArgumentException::class.java) {
      wallet.add(negativeMoney)
    }
  }

  @Test
  fun `removing no money the wallet should be the same - 05`() {
    val noMoney = Money(usd, 0.0)
    val walletWithMoney = wallet.add(tenDollars)
    val walletWithSameMoney = walletWithMoney.remove(noMoney)
    val balanceInDollars = walletWithSameMoney.getAllBalances()[usd]
    assert(balanceInDollars == 10.0)
  }

  @Test
  fun `removing negative balance throw exception - 06`() {
    val negativeMoney = Money(usd, -10.00)
    Assertions.assertThrows(IllegalArgumentException::class.java) {
      wallet.remove(negativeMoney)
    }
  }

  @Test
  fun `removing same balance as current balance should leave wallet with no balance - 07`() {
    val walletWithMoney = wallet.add(tenDollars)
    val walletWithNoMoney = walletWithMoney.remove(tenDollars)
    assert(walletWithNoMoney.getAllBalances()[usd] == 0.0)
  }

  @Test
  fun `removing more balance than actual should throw error - 08`() {
    val walletWithMoney = wallet.add(tenDollars)
    Assertions.assertThrows(IllegalArgumentException::class.java) {
      walletWithMoney.remove(Money(usd, 20.00))
    }
  }

  @Test
  fun `adding a currency and asking for another one should return correctly - 09`() {
    val walletWithMoney = wallet.add(tenDollars)
    val balances = walletWithMoney.getAllBalances()
    assert(balances.getOrDefault(ars, 0.0) == 0.0)
  }

  @Test
  fun `adding two currencies and asking for both should return correctly - 10`() {
    val walletWithMoney = wallet.add(tenDollars)
    val walletWithMoreMoney = walletWithMoney.add(tenPesos)
    assert(walletWithMoreMoney.getAllBalances()[ars] == 10.0)
    assert(walletWithMoreMoney.getAllBalances()[usd] == 10.0)
  }

  @Test
  fun `removing from empty wallet should throw exception - 11`() {
    Assertions.assertThrows(IllegalArgumentException::class.java) {
      wallet.remove(tenDollars)
    }
  }

  @Test
  fun `adding then removing partial amount should update balance correctly - 12`() {
    val walletWithMoney = wallet.add(tenDollars)
    val walletAfterPartialRemove = walletWithMoney.remove(Money(usd, 4.0))
    assert(walletAfterPartialRemove.getAllBalances()[usd] == 6.0)
  }

  @Test
  fun `adding zero money should not change balance - 13`() {
    val walletWithMoney = wallet.add(tenDollars)
    val walletAfterZeroAdd = walletWithMoney.add(Money(usd, 0.0))
    assert(walletAfterZeroAdd.getAllBalances()[usd] == 10.0)
  }

  @Test
  fun `removing all from multi-currency wallet should only affect one currency - 14`() {
    val walletWithBoth = wallet.add(tenDollars).add(tenPesos)
    val walletAfterRemove = walletWithBoth.remove(tenDollars)
    assert(walletAfterRemove.getAllBalances()[usd] == 0.0)
    assert(walletAfterRemove.getAllBalances()[ars] == 10.0)
  }

  @Test
  fun `adding and removing in sequence should maintain correct balances - 15`() {
    val wallet1 = wallet.add(tenDollars)
    val wallet2 = wallet1.add(Money(usd, 5.0))
    val wallet3 = wallet2.remove(Money(usd, 3.0))
    assert(wallet3.getAllBalances()[usd] == 12.0)
  }

  @Test
  fun `removing more than available in multi-currency wallet throws exception - 16`() {
    val walletWithBoth = wallet.add(tenDollars).add(tenPesos)
    Assertions.assertThrows(IllegalArgumentException::class.java) {
      walletWithBoth.remove(Money(usd, 20.0))
    }
  }

  @Test
  fun `adding money to one wallet does not affect another wallet - 17`() {
    val wallet2 = Wallet()
    val wallet1WithMoney = wallet.add(tenDollars)
    assert(wallet2.getAllBalances().getOrDefault(usd, 0.0) == 0.0)
    assert(wallet1WithMoney.getAllBalances()[usd] == 10.0)
  }

  @Test
  fun `removing zero from empty wallet does not throw - 18`() {
    val walletAfterRemove = wallet.remove(Money(usd, 0.0))
    assert(walletAfterRemove.getAllBalances()[usd] == 0.0)
  }

  @Test
  fun `adding and removing different currencies does not affect each other - 19`() {
    val walletWithBoth = wallet.add(tenDollars).add(tenPesos)
    val walletAfterRemove = walletWithBoth.remove(tenPesos)
    assert(walletAfterRemove.getAllBalances()[usd] == 10.0)
    assert(walletAfterRemove.getAllBalances()[ars] == 0.0)
  }

  @Test
  fun `adding money twice accumulates correctly - 20`() {
    val walletWithMoney = wallet.add(tenDollars).add(tenDollars)
    assert(walletWithMoney.getAllBalances()[usd] == 20.0)
  }

  @Test
  fun `removing all then adding again works - 21`() {
    val walletWithMoney = wallet.add(tenDollars)
    val walletEmpty = walletWithMoney.remove(tenDollars)
    val walletWithMoneyAgain = walletEmpty.add(tenDollars)
    assert(walletWithMoneyAgain.getAllBalances()[usd] == 10.0)
  }

  @Test
  fun `adding negative zero does not throw exception - 22`() {
    wallet.add(Money(usd, -0.0))
  }

  @Test
  fun `removing negative zero does not throw exception - 23`() {
    wallet.remove(Money(usd, -0.0))
  }

  @Test
  fun `adding and removing float values maintains precision - 24`() {
    val walletWithMoney = wallet.add(Money(usd, 10.123))
    val walletAfterRemove = walletWithMoney.remove(Money(usd, 0.123))
    assert(abs(walletAfterRemove.getAllBalances()[usd]!! - 10.0) < 0.0001)
  }

  @Test
  fun `removing from wallet with only other currency throws exception - 25`() {
    val walletWithPesos = wallet.add(tenPesos)
    Assertions.assertThrows(IllegalArgumentException::class.java) {
      walletWithPesos.remove(tenDollars)
    }
  }
}
