package com.justsend.api.dto

data class RegisterDto(val email: String, val password: String)

data class LoginDto(val email: String, val password: String)

data class LoginResponse(val token: String)
