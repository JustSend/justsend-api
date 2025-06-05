package com.justsend.api.repository

import com.justsend.api.entity.transaction.Transaction
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface TransactionRepository : JpaRepository<Transaction, UUID> {
  fun findAllByWallet_Id(walletId: String): List<Transaction>
}
