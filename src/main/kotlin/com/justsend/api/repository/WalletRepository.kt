package com.justsend.api.repository

import com.justsend.api.entity.WalletEntity
import org.springframework.data.jpa.repository.JpaRepository

interface WalletRepository : JpaRepository<WalletEntity, String>
