package com.justsend.api.dto

data class DepositRequest(
  val token: String,
  val amount: Amount,
  val currency: Currency
)
