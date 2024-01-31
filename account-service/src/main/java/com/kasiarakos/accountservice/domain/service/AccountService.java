package com.kasiarakos.accountservice.domain.service;

import com.kasiarakos.accountservice.adapters.dto.AccountDto;
import com.kasiarakos.accountservice.adapters.dto.CustomerDto;
import com.kasiarakos.accountservice.adapters.storage.entity.AccountEntity;
import com.kasiarakos.accountservice.adapters.storage.entity.CustomerEntity;
import com.kasiarakos.accountservice.adapters.storage.mapper.AccountMapper;
import com.kasiarakos.accountservice.adapters.storage.mapper.CustomerMapper;
import com.kasiarakos.accountservice.adapters.storage.repository.AccountRepository;
import com.kasiarakos.accountservice.adapters.storage.repository.CustomerRepository;
import com.kasiarakos.accountservice.domain.exception.CustomerAlreadyExistsException;
import com.kasiarakos.accountservice.domain.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

import static com.kasiarakos.accountservice.adapters.controller.constants.AccountConstants.ADDRESS;
import static com.kasiarakos.accountservice.adapters.controller.constants.AccountConstants.SAVINGS;

@Service
@AllArgsConstructor
public class AccountService {

  private final AccountRepository accountRepository;
  private final CustomerRepository customerRepository;

  public void createAccount(CustomerDto customerDto) {
    var customerEntity = CustomerMapper.toEntity(customerDto);
    var optionalCustomer =
        this.customerRepository.findByMobileNumber(customerEntity.getMobileNumber());
    if (optionalCustomer.isPresent()) {
      throw new CustomerAlreadyExistsException("already exists");
    }
    var savedCustomer = this.customerRepository.save(customerEntity);
    accountRepository.save(createAccount(savedCustomer));
  }

  public CustomerDto fetchCustomerByMobileNumber(String mobileNumber) {

    var customerEntity =
        this.customerRepository
            .findByMobileNumber(mobileNumber)
            .orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));

    var accountEntity =
        this.accountRepository
            .findByCustomerId(customerEntity.getCustomerId())
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        "Account", "mobileNumber", customerEntity.getCustomerId().toString()));
    var customerDto = CustomerMapper.toDto(customerEntity);
    customerDto.setAccount(AccountMapper.toDto(accountEntity));
    return customerDto;
  }

  public boolean updateAccount(CustomerDto customerDto) {
    AccountDto accountDto = customerDto.getAccount();
    if (accountDto != null) {
      AccountEntity accountEntity =
          this.accountRepository
              .findById(accountDto.getAccountNumber())
              .orElseThrow(
                  () ->
                      new ResourceNotFoundException(
                          "Account", "AccountNumber", accountDto.getAccountNumber().toString()));
      AccountMapper.updateEntity(accountDto, accountEntity);
      accountEntity = this.accountRepository.save(accountEntity);

      Long customerId = accountEntity.getCustomerId();
      CustomerEntity customer =
          this.customerRepository
              .findById(customerId)
              .orElseThrow(
                  () ->
                      new ResourceNotFoundException(
                          "Customer", "CustomerId", customerId.toString()));
      CustomerMapper.updateEntity(customerDto, customer);
      this.customerRepository.save(customer);
      return true;
    }
    return false;
  }

  public boolean deleteAccount(String mobileNumber) {
    var customerEntity =
        this.customerRepository
            .findByMobileNumber(mobileNumber)
            .orElseThrow(
                () -> new ResourceNotFoundException("Customer", "MobileNumber", mobileNumber));

    accountRepository.deleteByCustomerId(customerEntity.getCustomerId());
    customerRepository.deleteById(customerEntity.getCustomerId());
    return true;
  }

  private AccountEntity createAccount(CustomerEntity customer) {
    AccountEntity account = new AccountEntity();
    account.setCustomerId(customer.getCustomerId());
    long randomAccountNumber = 1_000_000_000 + new Random().nextInt(900_000_000);
    account.setAccountNumber(randomAccountNumber);
    account.setAccountType(SAVINGS);
    account.setBranchAddress(ADDRESS);
    return account;
  }
}
