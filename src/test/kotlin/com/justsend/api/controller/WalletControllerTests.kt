package com.justsend.api.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.justsend.api.dto.*
import com.justsend.api.entity.Wallet
import com.justsend.api.external.ExternalApiClient
import com.justsend.api.external.ValidationResponse
import com.justsend.api.service.AuthService
import com.justsend.api.service.WalletService
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import java.time.Instant
import java.util.UUID

class WalletControllerTests {

  private val walletService = mockk<WalletService>()
  private val externalApiClient = mockk<ExternalApiClient>()
  private val authService = mockk<AuthService>()

  private lateinit var mockMvc: MockMvc
  private val objectMapper = jacksonObjectMapper()

  @BeforeEach
  fun setup() {
    val controller = WalletController(walletService, externalApiClient, authService)
    mockMvc = MockMvcBuilders.standaloneSetup(controller).build()
  }

  @Test
  fun `getWalletBalances should return balances`() {
    val balances = mapOf("USD" to 100.0, "ARS" to 200.0)
    every { walletService.getBalances() } returns balances

    mockMvc.perform(get("/api/wallet"))
      .andExpect(status().isOk)
      .andExpect(jsonPath("USD").value(100.0))
      .andExpect(jsonPath("ARS").value(200.0))
  }

  @Test
  fun `debin should succeed when external validation is valid`() {
    val debinRequest = DebinRequest(50.0, "USD", "111111")
    val wallet = Wallet(alias = "test", email = "test@example.com")

    every { authService.getUserWallet() } returns wallet
    every { externalApiClient.validate(debinRequest) } returns ValidationResponse(true, "OK", "Validated")
    every { walletService.deposit(Money("USD", 50.0), wallet) } returns Result.success("Deposit successful")

    mockMvc.perform(
      post("/api/wallet/debin")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(debinRequest))
    )
      .andExpect(status().isOk)
      .andExpect(jsonPath("$.success").value(true))
      .andExpect(jsonPath("$.status").value("success"))
      .andExpect(jsonPath("$.message").value("Deposit successful"))
  }

  @Test
  fun `debin should fail when external validation fails`() {
    val debinRequest = DebinRequest(50.0, "USD", "222222222")
    val wallet = Wallet(alias = "test", email = "test@example.com")

    every { authService.getUserWallet() } returns wallet
    every { externalApiClient.validate(debinRequest) } returns ValidationResponse(false, "ERROR", "Invalid token")

    mockMvc.perform(
      post("/api/wallet/debin")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(debinRequest))
    )
      .andExpect(status().isBadRequest)
      .andExpect(jsonPath("$.success").value(false))
      .andExpect(jsonPath("$.status").value("ERROR"))
      .andExpect(jsonPath("$.message").value("Invalid token"))
  }

  @Test
  fun `withdraw should return success response`() {
    val money = Money("USD", 20.0)
    every { walletService.withdraw(money) } returns Result.success("Withdraw successful")

    mockMvc.perform(
      post("/api/wallet/withdraw")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(money))
    )
      .andExpect(status().isOk)
      .andExpect(jsonPath("$.success").value(true))
      .andExpect(jsonPath("$.message").value("Withdraw successful"))
  }

  @Test
  fun `withdraw should return failure response`() {
    val money = Money("USD", 9999.0)
    every { walletService.withdraw(money) } returns Result.failure(Exception("Insufficient funds"))

    mockMvc.perform(
      post("/api/wallet/withdraw")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(money))
    )
      .andExpect(status().isBadRequest)
      .andExpect(jsonPath("$.success").value(false))
      .andExpect(jsonPath("$.message").value("Insufficient funds"))
  }

  @Test
  fun `getWalletTransactions should return list of transactions`() {
    val transactions = listOf(
      TransactionDto(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), "123L", 100.0, "USD", TransactionType.DEPOSIT, Instant.now(), null),
      TransactionDto(UUID.fromString("123e4567-e89b-12d3-a456-426614174001"), "123L", 50.0, "USD", TransactionType.WITHDRAW, Instant.now(), null)
    )
    every { walletService.getTransactions() } returns transactions

    mockMvc.perform(get("/api/wallet/transactions"))
      .andExpect(status().isOk)
      .andExpect(jsonPath("$.length()").value(2))
      .andExpect(jsonPath("$[0].amount").value(100.0))
      .andExpect(jsonPath("$[1].amount").value(50.0))
  }

  @Test
  fun `sendTransaction should return success`() {
    val request = P2PTransaction(

      to = UserInfo(
        email = "other@example.com",
        alias = null
      ),
      money = Money("USD", 10.0)
    )

    every { walletService.send(request.money, request.to) } returns Result.success("Sent successfully")

    mockMvc.perform(
      post("/api/wallet/send")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request))
    )
      .andExpect(status().isOk)
      .andExpect(jsonPath("$.success").value(true))
      .andExpect(jsonPath("$.message").value("Sent successfully"))
  }

  @Test
  fun `sendTransaction should return failure`() {
    val request = P2PTransaction(
      money = Money("USD", 1000.0),
      to = UserInfo(alias = null, "other@example.com")
    )

    every { walletService.send(request.money, request.to) } returns Result.failure(Exception("Recipient not found"))

    mockMvc.perform(
      post("/api/wallet/send")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request))
    )
      .andExpect(status().isBadRequest)
      .andExpect(jsonPath("$.success").value(false))
      .andExpect(jsonPath("$.message").value("Recipient not found"))
  }
}
