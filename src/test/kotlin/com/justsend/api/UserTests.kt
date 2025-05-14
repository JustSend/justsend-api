package com.justsend.api

import com.justsend.api.model.User
import kotlin.test.Test

class UserTests {

  val john = User("John Doe")

  @Test
  fun `test user has a name`() {
    val johnName = john.name
    assert(johnName == "John Doe")
  }
}
