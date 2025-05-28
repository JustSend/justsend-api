// package com.justsend.api.service
//
// import com.justsend.api.dto.Amount
// import com.justsend.api.dto.Currency
// import com.justsend.api.dto.Money
// import com.justsend.api.mappers.WalletMapper
// import com.justsend.api.repository.UserRepository
// import com.justsend.api.repository.WalletRepository
// import org.springframework.stereotype.Service
//
// @Service
// class WalletService(
//  private val userRepository: UserRepository,
//  private val walletRepository: WalletRepository,
//  private val walletMapper: WalletMapper
// ) {
//
//  fun getBalances(): Map<Currency, Amount> {
//    val user = securityService.getUser()
//    return user.wallet.balances
//  }
//
//  fun addBalance(money: Money): Result<String> {
//    val user = securityService.getUser()
//
//    return try {
//      val updatedWallet = walletMapper.toDomain(user.wallet).add(money)
//      user.wallet = walletMapper.toEntity(updatedWallet)
//      userRepository.save(user)
//      Result.success("Added ${money.amount} ${money.currency} to wallet successfully")
//    } catch (ex: IllegalArgumentException) {
//      Result.failure(ex)
//    }
//  }
//
//  fun removeBalance(money: Money) {
//    val user = securityService.getUser()
//    val updatedWallet = walletMapper.toDomain(user.wallet).remove(money)
//    user.wallet = walletMapper.toEntity(updatedWallet)
//    userRepository.save(user)
//  }
// }
