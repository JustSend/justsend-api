package com.justsend.api.mappers

import com.justsend.api.entity.WalletEntity
import com.justsend.api.model.Wallet

fun WalletEntity.toDomain(): Wallet {
  return Wallet(balances)
}

fun Wallet.toEntity(): WalletEntity {
  return WalletEntity(id = null, balances = this.getAllBalances())
}
