package com.kasiarakos.accountservice.adapters.storage.mapper;

import com.kasiarakos.accountservice.adapters.dto.AccountDto;
import com.kasiarakos.accountservice.adapters.storage.entity.AccountEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountMapper {
  public static AccountDto toDto(AccountEntity accountEntity) {
    AccountDto accountDto = new AccountDto();
    accountDto.setAccountNumber(accountEntity.getAccountNumber());
    accountDto.setBranchAddress(accountEntity.getBranchAddress());
    accountDto.setAccountType(accountEntity.getAccountType());
    return accountDto;
  }

  public static AccountEntity toEntity(AccountDto accountDto) {
    return updateEntity(accountDto, new AccountEntity());
  }

  public static AccountEntity updateEntity(AccountDto accountDto, AccountEntity accountEntity) {
    accountEntity.setAccountNumber(accountDto.getAccountNumber());
    accountEntity.setBranchAddress(accountDto.getBranchAddress());
    accountEntity.setAccountType(accountDto.getAccountType());
    return accountEntity;
  }
}
