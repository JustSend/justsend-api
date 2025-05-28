package com.justsend.api.service

import com.justsend.api.entity.User
import org.springframework.stereotype.Service

@Service
class SecurityService {

  fun getUser(): User = User()
}
