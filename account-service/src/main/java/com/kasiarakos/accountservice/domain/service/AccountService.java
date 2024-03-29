package com.kasiarakos.accountservice.domain.service;

import com.kasiarakos.accountservice.adapters.dto.AccountDto;
import com.kasiarakos.accountservice.adapters.dto.AccountMessageDto;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import java.util.Random;

import static com.kasiarakos.accountservice.adapters.controller.constants.AccountConstants.ADDRESS;
import static com.kasiarakos.accountservice.adapters.controller.constants.AccountConstants.SAVINGS;

@Slf4j
@Service
@AllArgsConstructor
public class AccountService {

  private final AccountRepository accountRepository;
  private final CustomerRepository customerRepository;
  private final StreamBridge streamBridge;

  public void createAccount(CustomerDto customerDto) {
    var customerEntity = CustomerMapper.toEntity(customerDto);
    var optionalCustomer =
        this.customerRepository.findByMobileNumber(customerEntity.getMobileNumber());
    if (optionalCustomer.isPresent()) {
      throw new CustomerAlreadyExistsException("already exists");
    }
    var savedCustomer = this.customerRepository.save(customerEntity);
    var accountEntity = accountRepository.save(createAccount(savedCustomer));
    sendCommunication(accountEntity, savedCustomer);
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

  public boolean updateCommunicationStatus(Long accountNumber) {
    boolean isUpdated = false;
    if(accountNumber !=null ){
      AccountEntity accountEntity = accountRepository.findById(accountNumber).orElseThrow(
              () -> new ResourceNotFoundException("Account", "AccountNumber", accountNumber.toString())
      );
      accountEntity.setCommunicationSw(true);
      accountRepository.save(accountEntity);
      isUpdated = true;
    }
    return  isUpdated;
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

  private void sendCommunication(AccountEntity account, CustomerEntity customer) {
    var accountsMsgDto = new AccountMessageDto(account.getAccountNumber(), customer.getName(),
            customer.getEmail(), customer.getMobileNumber());
    log.info("Sending Communication request for the details: {}", accountsMsgDto);
    var result = streamBridge.send("sendCommunication-out-0", accountsMsgDto);
    log.info("Is the Communication request successfully triggered ? : {}", result);
  }
}
