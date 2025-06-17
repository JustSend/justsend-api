package com.justsend.api.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.justsend.api.dto.User
import com.justsend.api.entity.Wallet
import com.justsend.api.service.AuthService
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders

class UserControllerTests {

  private val authService = mockk<AuthService>()
  private val controller = UserController(authService)
  private val mockMvc: MockMvc = MockMvcBuilders.standaloneSetup(controller).build()
  private val objectMapper = jacksonObjectMapper()

  @Test
  fun `register should return 200 OK on success`() {
    val user = User(uid = "1", email = "test@example.com")
    every { authService.register(user) } returns Result.success("Registered")

    mockMvc.perform(
      post("/api/user/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(user))
    )
      .andExpect(status().isOk)
      .andExpect(content().string("Registered"))
  }

  @Test
  fun `register should return 400 Bad Request on failure`() {
    val user = User(uid = "1", email = "fail@example.com")
    every { authService.register(user) } returns Result.failure(Exception("Already exists"))

    mockMvc.perform(
      post("/api/user/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(user))
    )
      .andExpect(status().isBadRequest)
      .andExpect(content().string("Already exists"))
  }

  @Test
  fun `getWalletInfo should return user info`() {
    val wallet = Wallet(alias = "testAlias", email = "test@example.com")
    every { authService.getUserWallet() } returns wallet

    mockMvc.perform(get("/api/user/me"))
      .andExpect(status().isOk)
      .andExpect(jsonPath("$.alias").value("testAlias"))
      .andExpect(jsonPath("$.email").value("test@example.com"))
  }

  @Test
  fun `getAllOtherUsers should return filtered users`() {
    val currentUser = Wallet(alias = "me", email = "me@example.com")
    val users = listOf(
      Wallet(alias = "other1", email = "other1@example.com"),
      Wallet(alias = "other2", email = "other2@example.com"),
      Wallet(alias = "me", email = "me@example.com")
    )

    every { authService.getUserWallet() } returns currentUser
    every { authService.getAllUsers() } returns users

    mockMvc.perform(get("/api/user/search"))
      .andExpect(status().isOk)
      .andExpect(jsonPath("$.length()").value(2))
      .andExpect(jsonPath("$[0].alias").value("other1"))
      .andExpect(jsonPath("$[1].alias").value("other2"))
  }

  @Test
  fun `getAllOtherUsers should filter by query`() {
    val currentUser = Wallet(alias = "me", email = "me@example.com")
    val users = listOf(
      Wallet(alias = "John", email = "john@example.com"),
      Wallet(alias = "Jane", email = "jane@example.com")
    )

    every { authService.getUserWallet() } returns currentUser
    every { authService.getAllUsers() } returns users

    mockMvc.perform(get("/api/user/search").param("query", "jane"))
      .andExpect(status().isOk)
      .andExpect(jsonPath("$.length()").value(1))
      .andExpect(jsonPath("$[0].alias").value("Jane"))
  }
}
