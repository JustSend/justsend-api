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
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.fold

@RestController()
@RequestMapping("/api/wallet")
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

  @PostMapping("/deposit")
  fun addMoney(@RequestBody body: Money): ResponseEntity<String> {
    val result = walletService.deposit(body)
    return result.fold(
      onSuccess = { successMessage -> ResponseEntity.ok(successMessage) },
      onFailure = { error -> ResponseEntity.badRequest().body(error.message ?: "Unknown error") }
    )
  }

  @PostMapping("/withdraw")
  fun removeMoney(@RequestBody body: Money) {
    walletService.withdraw(body)
  }
}
