package com.justsend.api.service

import com.justsend.api.entity.User
import com.justsend.api.entity.WalletEntity
import com.justsend.api.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {

  fun updateWallet(user: User, walletEntity: WalletEntity) {
    user.wallet = walletEntity
    userRepository.save(user)
  }
}
