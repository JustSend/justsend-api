package com.justsend.api.dto

data class DebinRequest(
  val amount: Amount,
  val currency: Currency,
  val bank_routing: String
)

data class DepositRequest(
  val amount: Amount,
  val currency: Currency,
  val walletId: String
)

data class DebinResponse(
  val success: Boolean,
  val status: String,
  val message: String
)

data class DepositResponse(
  val success: Boolean,
  val message: String
)
