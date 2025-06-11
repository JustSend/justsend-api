package com.justsend.api.dto

data class User(
  val uid: String,
  val email: String
)

data class UserInfo(
  val alias: String?,
  val email: String?
)
