package com.justsend.api.model

typealias Currency = String
typealias Amount = Double

data class Money(val currency: Currency, val amount: Amount)
