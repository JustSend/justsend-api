package com.justsend.api.repository

import com.justsend.api.entity.Wallet
import org.springframework.data.jpa.repository.JpaRepository

interface WalletRepository : JpaRepository<Wallet, String>
