package com.justsend.api.dto

data class P2PTransaction(
  val to: P2PUser,
  val money: Money
)

data class SendResponse(val success: Boolean, val message: String)
