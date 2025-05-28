package com.justsend.api.repository

import com.justsend.api.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, String>
