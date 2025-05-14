package com.justsend.api.controller

import com.justsend.api.model.User
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController("/user")
class UserController {

  @PostMapping
  fun createAccount(
    @RequestBody @Validated
    user: User
  ): User {
    return user
  }
}
