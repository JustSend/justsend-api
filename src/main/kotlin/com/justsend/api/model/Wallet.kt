package com.justsend.api.model

class Wallet(private var balance: Int = 0) {

  fun addBalance(amount: Int) {
    require(amount >= 0) { "Amount must be non-negative" }
    balance += amount
  }

  fun getBalance(): Int = balance
}
