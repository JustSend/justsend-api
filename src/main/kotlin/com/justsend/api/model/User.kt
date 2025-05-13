package com.justsend.api.model

class User(val name: String) {

  private val wallet = Wallet()

  fun getWallet(): Wallet {
    return wallet
  }
}
