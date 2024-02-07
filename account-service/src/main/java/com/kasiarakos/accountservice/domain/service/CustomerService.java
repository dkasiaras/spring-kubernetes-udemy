package com.kasiarakos.accountservice.domain.service;

import com.kasiarakos.accountservice.adapters.dto.CustomerDetailsDto;
import com.kasiarakos.accountservice.adapters.external.CardsClient;
import com.kasiarakos.accountservice.adapters.external.CardsDto;
import com.kasiarakos.accountservice.adapters.external.LoansClient;
import com.kasiarakos.accountservice.adapters.external.LoansDto;
import com.kasiarakos.accountservice.adapters.storage.mapper.AccountMapper;
import com.kasiarakos.accountservice.adapters.storage.mapper.CustomerMapper;
import com.kasiarakos.accountservice.adapters.storage.repository.AccountRepository;
import com.kasiarakos.accountservice.adapters.storage.repository.CustomerRepository;
import com.kasiarakos.accountservice.domain.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerService {

  private final AccountRepository accountRepository;
  private final CustomerRepository customerRepository;
  private final CardsClient cardsClient;
  private final LoansClient loansClient;

  public CustomerDetailsDto fetchCustomerDetails(String mobileNumber) {
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

    var customerDetailsDto = CustomerMapper.toCustomerDetailsDto(customerEntity, new CustomerDetailsDto());
    customerDetailsDto.setAccount(AccountMapper.toDto(accountEntity));

    ResponseEntity<LoansDto> loansDtoResponseEntity = this.loansClient.fetchLoanDetails(mobileNumber);
    if(null != loansDtoResponseEntity) {
      customerDetailsDto.setLoans(loansDtoResponseEntity.getBody());
    }

    ResponseEntity<CardsDto> cardsDtoResponseEntity =this.cardsClient.fetchCardDetails(mobileNumber);
    if(cardsDtoResponseEntity != null) {
      customerDetailsDto.setCards(cardsDtoResponseEntity.getBody());
    }

    return customerDetailsDto;
  }
}
