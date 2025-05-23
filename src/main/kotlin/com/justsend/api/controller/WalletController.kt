package com.justsend.api.controller

import com.justsend.api.dto.Amount
import com.justsend.api.dto.Currency
import com.justsend.api.dto.Money
import com.justsend.api.service.WalletService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import kotlin.fold

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
  fun addMoney(@RequestBody body: Money): ResponseEntity<String> {
    val result = walletService.addBalance(body)
    return result.fold(
      onSuccess = { successMessage -> ResponseEntity.ok(successMessage) },
      onFailure = { error -> ResponseEntity.badRequest().body(error.message ?: "Unknown error") }
    )
  }

  @PostMapping("/remove")
  fun removeMoney(@RequestBody body: Money) {
    walletService.removeBalance(body)
  }
}
