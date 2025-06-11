package com.justsend.api.dto

data class User(
  val uid: String,
  val email: String
)

data class P2PUser(
  val alias: String?,
  val email: String?
)
