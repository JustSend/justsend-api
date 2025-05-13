package com.justsend.api

import com.justsend.api.model.User
import kotlin.test.Test

class UserTests {

  @Test
  fun `new user should have a new empty wallet`() {
    val john = User("john")
    val wallet = john.getWallet()
    assert(wallet.getBalance("USD") == 0.0)
    assert(wallet.getBalance("ARS") == 0.0)
  }
}
