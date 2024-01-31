package com.kasiarakos.accountservice.adapters.storage.mapper;

import com.kasiarakos.accountservice.adapters.dto.CustomerDetailsDto;
import com.kasiarakos.accountservice.adapters.dto.CustomerDto;
import com.kasiarakos.accountservice.adapters.storage.entity.CustomerEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomerMapper {

  public static CustomerDto toDto(CustomerEntity customerEntity) {
    CustomerDto customerDto = new CustomerDto();
    customerDto.setEmail(customerEntity.getEmail());
    customerDto.setName(customerEntity.getName());
    customerDto.setMobileNumber(customerEntity.getMobileNumber());
    return customerDto;
  }

  public static CustomerDetailsDto toCustomerDetailsDto(
      CustomerEntity customerEntity, CustomerDetailsDto customerDetails) {
    customerDetails.setEmail(customerEntity.getEmail());
    customerDetails.setName(customerEntity.getName());
    customerDetails.setMobileNumber(customerEntity.getMobileNumber());
    return customerDetails;
  }

  public static CustomerEntity toEntity(CustomerDto customerDto) {
    return updateEntity(customerDto, new CustomerEntity());
  }

  public static CustomerEntity updateEntity(
      CustomerDto customerDto, CustomerEntity customerEntity) {
    customerEntity.setEmail(customerDto.getEmail());
    customerEntity.setName(customerDto.getName());
    customerEntity.setMobileNumber(customerDto.getMobileNumber());
    return customerEntity;
  }
}
