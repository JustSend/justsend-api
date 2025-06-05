// package com.justsend.api.entity.transaction
//
// import com.justsend.api.dto.Amount
// import com.justsend.api.dto.Currency
// import com.justsend.api.dto.TransactionType
// import com.justsend.api.entity.Wallet
// import jakarta.persistence.DiscriminatorValue
// import jakarta.persistence.Entity
// import jakarta.persistence.FetchType
// import jakarta.persistence.JoinColumn
// import jakarta.persistence.ManyToOne
// import java.time.Instant
//
// @Entity
// @DiscriminatorValue("P2P")
// class P2PTransaction(
//  wallet: Wallet,
//  amount: Amount,
//  currency: Currency,
//  @ManyToOne(fetch = FetchType.LAZY)
//  @JoinColumn(name = "recipient_wallet_id", nullable = false)
//  val recipientWallet: Wallet,
//  timestamp: Instant = Instant.now()
// ) : Transaction(
//  wallet = wallet,
//  amount = amount,
//  currency = currency,
//  type = TransactionType.P2P,
//  timestamp = timestamp
// )
