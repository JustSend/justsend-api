package com.justsend.api.repository

import com.justsend.api.entity.Wallet
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface WalletRepository : JpaRepository<Wallet, String> {
  @Query(
    """
        SELECT w FROM Wallet w
        WHERE (:email IS NOT NULL AND w.email = :email)
           OR (:alias IS NOT NULL AND w.alias = :alias)
    """
  )
  fun findByEmailOrAlias(email: String?, alias: String?): Wallet?
}
