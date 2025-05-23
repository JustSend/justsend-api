package com.justsend.api.controller

import com.justsend.api.model.Amount
import com.justsend.api.model.Currency
import com.justsend.api.service.WalletService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController("/wallet")
class WalletController(
  val walletService: WalletService
) {

  @GetMapping
  fun getWalletBalances(): ResponseEntity<Map<Currency, Amount>> {
    val balances = walletService.getBalances()
    return ResponseEntity.ok(balances)
  }

  @GetMapping("/{currency}")
  fun getWalletBalanceInCurrency(@PathVariable currency: String) {}

  // Estos 2 llamarían a la API externa, por ahora hago mock de interaction
  @PostMapping("/add")
  fun addMoney() {}

  @PostMapping("/remove")
  fun removeMoney() {}
}
