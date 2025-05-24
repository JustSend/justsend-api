package com.justsend.api.repository

import com.justsend.api.entity.WalletEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface WalletRepository : JpaRepository<WalletEntity, UUID>
