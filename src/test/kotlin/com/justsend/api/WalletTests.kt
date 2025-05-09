package com.justsend.api

import com.justsend.api.model.Wallet
import org.junit.jupiter.api.Assertions.assertThrows
import kotlin.test.Test

class WalletTests {

    @Test
    fun `new wallet should start with no balance`(){
        val wallet = Wallet()
        assert(wallet.getBalance() == 0)
    }

    @Test
    fun `adding positive balance to the wallet the wallet balance should not be cero`(){
        val wallet = Wallet()
        wallet.addBalance(10)
        assert(wallet.getBalance() != 0)
    }

    @Test
    fun `adding positive balance to wallet the wallet should have the new balance`(){
        val wallet = Wallet()
        wallet.addBalance(10)
        assert(wallet.getBalance() != 0)
    }

    @Test
    fun `adding negative balance to wallet should throw exception`() {
        val wallet = Wallet()

        assertThrows(IllegalArgumentException::class.java) {
            wallet.addBalance(-10)
        }
    }


}