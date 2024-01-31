package com.kasiarakos.accountservice.adapters.dto;


import com.kasiarakos.accountservice.adapters.external.CardsDto;
import com.kasiarakos.accountservice.adapters.external.LoansDto;
import lombok.Data;

@Data
public class CustomerDetailsDto {
  
  private String name;
  private String email;
  private String mobileNumber;
  private AccountDto account;
  private LoansDto loans;
  private CardsDto cards;
}
