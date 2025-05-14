package com.justsend.api.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController("/wallet")
class WalletController {

  @PostMapping("/create")
  fun createWallet() {}
}
