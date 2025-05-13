package com.justsend.api.model

class Wallet(private var balance: Int = 0) {

  fun addBalance(amount: Int) {
    if (amount < 0) {
      throw IllegalArgumentException("Amount cannot be negative")
    }
    balance += amount
  }

  fun removeBalance(amount: Int) {
    if (amount < 0) {
      throw IllegalArgumentException("Amount cannot be negative")
    }
    if (balance - amount < 0) {
      throw IllegalArgumentException("Cannot remove balance $amount")
    }
    balance -= amount
  }

  fun getBalance(): Int = balance
}
