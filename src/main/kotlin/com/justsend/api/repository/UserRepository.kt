package com.justsend.api.repository

import com.justsend.api.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface UserRepository : JpaRepository<User, UUID> {
  fun existsByEmail(email: String): Boolean
}
