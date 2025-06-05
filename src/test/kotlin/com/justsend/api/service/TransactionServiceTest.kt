package com.justsend.api.service

import com.justsend.api.entity.Wallet
import com.justsend.api.repository.WalletRepository
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import

@DataJpaTest
@Import(TransactionService::class, AuthService::class)
class TransactionServiceTest @Autowired constructor(
  val walletRepository: WalletRepository
) {
  lateinit var wallet: Wallet

  @BeforeEach
  fun setup() {
    wallet = walletRepository.save(Wallet())
  }

//    @Test
//    fun `createTransaction should persist a transaction`() {
//        val money = Money("ARS", 30000.0)
//        val transaction = transactionService.createTransaction(wallet, money, TransactionType.DEPOSIT)
//
//        assertNotNull(transaction.id)
//        assertEquals(wallet.id, transaction.wallet.id)
//        assertEquals(money.amount, transaction.amount)
//        assertEquals(money.currency, transaction.currency)
//        assertEquals(TransactionType.DEPOSIT, transaction.type)
//        assertNotNull(transaction.timestamp)
//
//        val found = transactionRepository.findById(transaction.id!!)
//        assertTrue(found.isPresent)
//    }

//  @Test
//    fun `making a deposit should create a deposit transaction`() {
//    }

  //  @Test
//    fun `making a withdrawal should create a withdrawal transaction`() {
//    }
}
