package com.justsend.api.mappers

import com.justsend.api.domain.Wallet
import com.justsend.api.entity.WalletEntity
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface WalletMapper {

  fun toDomain(walletEntity: WalletEntity): Wallet

  fun toEntity(wallet: Wallet): WalletEntity
}
