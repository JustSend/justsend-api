package com.justsend.api.controller

import com.justsend.api.dto.Amount
import com.justsend.api.dto.Currency
import com.justsend.api.dto.DebinRequest
import com.justsend.api.dto.DebinResponse
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

@RestController
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

  @PostMapping("/debin")
  fun debin(@RequestBody body: DebinRequest): ResponseEntity<DebinResponse> {
    val wallet = authService.getUserWallet()
    val externalValidation = externalApiClient.validate(body)

    if (!externalValidation.valid) {
      return ResponseEntity
        .badRequest()
        .body(DebinResponse(false, externalValidation.status, externalValidation.message))
    }

    val money = Money(body.currency, body.amount)
    val result = walletService.deposit(money, wallet)

    return result.fold(
      onSuccess = { successMessage ->
        ResponseEntity.ok(DebinResponse(true, "success", successMessage))
      },
      onFailure = { error ->
        ResponseEntity.status(500).body(
          DebinResponse(false, "failure", error.message ?: "Unknown error")
        )
      }
    )
  }

  @PostMapping("/deposit")
  fun deposit(@RequestBody body: DepositRequest): ResponseEntity<DepositResponse> {
    val wallet = authService.getUserWallet(body.walletId)
    val money = Money(body.currency, body.amount)
    val result = walletService.deposit(money, wallet!!)

    return result.fold(
      onSuccess = { successMessage ->
        ResponseEntity.ok(DepositResponse(true, successMessage))
      },
      onFailure = { error ->
        ResponseEntity.status(500).body(
          DepositResponse(false, error.message ?: "Unknown error")
        )
      }
    )
  }

  @PostMapping("/withdraw")
  fun withdraw(@RequestBody body: Money): ResponseEntity<SendResponse> {
    val result = walletService.withdraw(body)

    return result.fold(
      onSuccess = { successMessage ->
        ResponseEntity.ok(SendResponse(true, successMessage))
      },
      onFailure = { error ->
        ResponseEntity.status(400).body(
          SendResponse(false, error.message ?: "Unknown error")
        )
      }
    )
  }

  @GetMapping("/transactions")
  fun getWalletTransactions(): ResponseEntity<List<TransactionDto>> {
    val transactions = walletService.getTransactions()
    return ResponseEntity.ok(transactions)
  }

  @PostMapping("/send")
  fun sendTransaction(@RequestBody request: P2PTransaction): ResponseEntity<SendResponse> {
    val result = walletService.send(request.money, request.to)

    return result.fold(
      onSuccess = { successMessage ->
        ResponseEntity.ok(SendResponse(true, successMessage))
      },
      onFailure = { error ->
        ResponseEntity.status(400).body(
          SendResponse(false, error.message ?: "Unknown error")
        )
      }
    )
  }
}
