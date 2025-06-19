package com.justsend.api.external

import com.justsend.api.controller.WalletController
import com.justsend.api.service.AuthService
import com.justsend.api.service.WalletService
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@Tag("external")
class ExternalApiTests {

  private val walletService = mockk<WalletService>()
  private val authService = mockk<AuthService>()

  private lateinit var mockMvc: MockMvc

  @BeforeEach
  fun setup() {
    val realExternalApiClient = ExternalApiClient("http://localhost:8000")
    val controller = WalletController(walletService, realExternalApiClient, authService)
    mockMvc = MockMvcBuilders.standaloneSetup(controller).build()
  }

  @Test
  fun `should return 404 for unknown endpoint`() {
    mockMvc.perform(
      MockMvcRequestBuilders.get("/api/wallet/unknown-endpoint")
        .accept(MediaType.APPLICATION_JSON)
    )
      .andExpect(MockMvcResultMatchers.status().isNotFound)
  }

  @Test
  fun `should return 400 for malformed request`() {
    mockMvc.perform(
      MockMvcRequestBuilders.post("/api/wallet/send")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"invalid\":\"data\"}")
    )
      .andExpect(MockMvcResultMatchers.status().isBadRequest)
  }

  @Test
  fun `should return 400 for missing required fields`() {
    val incompleteRequest = "{\"amount\": 100}"
    mockMvc.perform(
      MockMvcRequestBuilders.post("/api/wallet/send")
        .contentType(MediaType.APPLICATION_JSON)
        .content(incompleteRequest)
    )
      .andExpect(MockMvcResultMatchers.status().isBadRequest)
  }
}
