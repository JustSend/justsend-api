package com.justsend.api.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController("/wallet")
class WalletController {

  @GetMapping
  fun getWallet() {}

  @GetMapping("/{currency}")
  fun getWalletBalanceInCurrency(@PathVariable currency: String) {}

  @PostMapping("/add")
  fun addMoney() {}

  @PostMapping("/remove")
  fun removeMoney() {}
}
