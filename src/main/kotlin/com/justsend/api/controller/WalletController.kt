package com.justsend.api.controller

import com.justsend.api.dto.Amount
import com.justsend.api.dto.Currency
import com.justsend.api.dto.DepositRequest
import com.justsend.api.dto.DepositResponse
import com.justsend.api.dto.Money
import com.justsend.api.dto.P2PTransaction
import com.justsend.api.dto.SendResponse
import com.justsend.api.dto.TransactionDto
import com.justsend.api.external.ExternalApiClient
import com.justsend.api.service.AuthService
import com.justsend.api.service.WalletService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.fold

@RestController()
@RequestMapping("/api/wallet")
class WalletController(
  @Autowired private val walletService: WalletService,
  @Autowired private val externalApiClient: ExternalApiClient,
  @Autowired private val authService: AuthService
) {

  @GetMapping
  fun getWalletBalances(): ResponseEntity<Map<Currency, Amount>> {
    val balances = walletService.getBalances()
    return ResponseEntity.ok(balances)
  }

  @PostMapping("/deposit")
  fun deposit(@RequestBody body: DepositRequest): DepositResponse {
    val wallet = authService.getUserWallet()

    val externalValidation = externalApiClient.validate(body)
    if (!externalValidation.valid) {
      return DepositResponse(false, externalValidation.status, externalValidation.message)
    }

    val money = Money(body.currency, body.amount)
    val result = walletService.deposit(money, wallet)

    return result.fold(
      onSuccess = { successMessage ->
        DepositResponse(true, "success", successMessage)
      },
      onFailure = { error ->
        DepositResponse(false, "failure", error.message ?: "Unknown error")
      }
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

  @PostMapping("/send")
  fun sendTransaction(@RequestBody request: P2PTransaction): SendResponse {
    val result = walletService.send(request.money, request.to)
    return result.fold(
      onSuccess = { successMessage ->
        SendResponse(true, successMessage)
      },
      onFailure = { error -> SendResponse(false, error.message ?: "Unknown error") }
    )
  }
}
