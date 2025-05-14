package com.justsend.api.service

import com.justsend.api.entity.WalletEntity
import com.justsend.api.model.Wallet
import com.justsend.api.repository.WalletRepository
import org.springframework.stereotype.Service

@Service
class WalletService(private val walletRepository: WalletRepository) {

  fun saveWallet(wallet: Wallet, id: String): WalletEntity {
    val entity = WalletEntity.from(wallet, id)
    return walletRepository.save(entity)
  }

  fun getWallet(id: String): Wallet? {
    return walletRepository.findById(id)
      .map { it.toWallet() }
      .orElse(null)
  }
}
