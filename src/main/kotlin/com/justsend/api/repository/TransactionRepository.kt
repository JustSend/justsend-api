package com.justsend.api.repository

import com.justsend.api.entity.TransactionEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface TransactionRepository : JpaRepository<TransactionEntity, UUID>
