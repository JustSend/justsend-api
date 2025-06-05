package com.justsend.api.controller

import com.justsend.api.dto.Amount
import com.justsend.api.dto.Currency
import com.justsend.api.dto.Money
import com.justsend.api.dto.TransactionDto
import com.justsend.api.service.WalletService
import org.springframework.beans.factory.annotation.Autowired
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
  @Autowired private val walletService: WalletService
) {

  @GetMapping
  fun getWalletBalances(): ResponseEntity<Map<Currency, Amount>> {
    val balances = walletService.getBalances()
    return ResponseEntity.ok(balances)
  }

  @GetMapping("/{currency}")
  fun getWalletBalanceInCurrency(@PathVariable currency: String): ResponseEntity<Amount> {
    val balance = walletService.getBalanceIn(currency)
    return ResponseEntity.ok(balance)
  }

  @PostMapping("/deposit")
  fun deposit(@RequestBody body: Money): ResponseEntity<String> {
    val result = walletService.deposit(body)
    return result.fold(
      onSuccess = { successMessage -> ResponseEntity.ok(successMessage) },
      onFailure = { error -> ResponseEntity.badRequest().body(error.message ?: "Unknown error") }
    )
  }

  @PostMapping("/withdraw")
  fun withdraw(@RequestBody body: Money) {
    walletService.withdraw(body)
  }

  @GetMapping("/transactions")
  fun getWalletTransactions(): ResponseEntity<List<TransactionDto>> {
    val body = walletService.getTransactions()
    return ResponseEntity.ok(body)
  }
}
