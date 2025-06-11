package com.justsend.api.dto

data class DepositRequest(
  val amount: Amount,
  val currency: Currency,
  val bank_routing: String
)

data class DepositResponse(
  val success: Boolean,
  val status: String,
  val message: String
)
