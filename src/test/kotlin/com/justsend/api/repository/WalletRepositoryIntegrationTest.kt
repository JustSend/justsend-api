package com.justsend.api.repository

import com.justsend.api.entity.Wallet
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class WalletRepositoryIntegrationTest @Autowired constructor(
  val walletRepository: WalletRepository
) {
  @Test
  fun `should save and retrieve wallet`() {
    val wallet = Wallet(
      id = "test-user-id",
      email = "test@example.com",
      alias = "testalias",
      balances = mutableMapOf("USD" to 100.0)
    )
    walletRepository.save(wallet)
    val found = walletRepository.findById("test-user-id").get()
    assertEquals(wallet.email, found.email)
    assertEquals(wallet.balances["USD"], found.balances["USD"])
  }

  @Test
  fun `should update wallet balances`() {
    val wallet = Wallet(
      id = "user-2",
      email = "user2@example.com",
      alias = "alias2",
      balances = mutableMapOf("EUR" to 50.0)
    )
    walletRepository.save(wallet)
    val toUpdate = walletRepository.findById("user-2").get()
    toUpdate.balances["EUR"] = 75.0
    walletRepository.save(toUpdate)
    val updated = walletRepository.findById("user-2").get()
    assertEquals(75.0, updated.balances["EUR"])
  }

  @Test
  fun `should delete wallet`() {
    val wallet = Wallet(
      id = "user-3",
      email = "user3@example.com",
      alias = "alias3",
      balances = mutableMapOf("ARS" to 200.0)
    )
    walletRepository.save(wallet)
    walletRepository.deleteById("user-3")
    val found = walletRepository.findById("user-3")
    assertEquals(false, found.isPresent)
  }
}
