package com.justsend.api.mappers

import com.justsend.api.domain.Transaction
import com.justsend.api.entity.TransactionEntity
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface TransactionMapper {

  fun toDomain(transactionEntity: TransactionEntity): Transaction

  fun toEntity(transaction: Transaction): TransactionEntity
}
