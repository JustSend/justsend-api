package com.justsend.api

import com.justsend.api.domain.Wallet
import com.justsend.api.dto.Money
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
    assert(wallet.getBalanceIn(usd) == 0.0)
    assert(wallet.getBalanceIn(ars) == 0.0)
  }

  @Test
  fun `adding positive balance to the wallet the wallet balance should not be cero - 02`() {
    val newWallet = wallet.add(tenDollars)
    val balanceInDollars = newWallet.getBalanceIn(usd)
    assert(balanceInDollars != 0.0)
  }

  @Test
  fun `adding positive balance to wallet the wallet should have the new balance - 03`() {
    val newWallet = wallet.add(tenDollars)
    val balanceInDollars = newWallet.getBalanceIn(usd)
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
    val walletWithMoney = wallet.add(tenDollars)
    val walletWithSameMoney = walletWithMoney.remove(noMoney)
    val balanceInDollars = walletWithSameMoney.getBalanceIn(usd)
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
    val walletWithMoney = wallet.add(tenDollars)
    val walletWithNoMoney = walletWithMoney.remove(tenDollars)
    assert(walletWithNoMoney.getBalanceIn(usd) == 0.0)
  }

  @Test
  fun `removing more balance than actual should throw error - 08`() {
    val walletWithMoney = wallet.add(tenDollars)
    assertThrows(IllegalArgumentException::class.java) {
      walletWithMoney.remove(Money(usd, 20.00))
    }
  }

  @Test
  fun `adding a currency and asking for another one should return correctly`() {
    val walletWithMoney = wallet.add(tenDollars)
    assert(walletWithMoney.getBalanceIn(ars) == 0.0)
  }

  @Test
  fun `adding two currencies and asking for both should return correctly`() {
    val walletWithMoney = wallet.add(tenDollars)
    val walletWithMoreMoney = walletWithMoney.add(tenPesos)
    assert(walletWithMoreMoney.getBalanceIn(ars) == 10.0)
    assert(walletWithMoreMoney.getBalanceIn(usd) == 10.0)
  }
}
