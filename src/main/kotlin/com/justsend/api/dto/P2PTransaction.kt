package com.justsend.api.dto

data class P2PTransaction(
  val to: UserInfo,
  val money: Money
)

data class SendResponse(val success: Boolean, val message: String)
