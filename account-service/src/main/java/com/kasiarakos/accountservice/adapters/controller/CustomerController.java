package com.kasiarakos.accountservice.adapters.controller;

import com.kasiarakos.accountservice.adapters.dto.CustomerDetailsDto;
import com.kasiarakos.accountservice.domain.service.CustomerService;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/customer-details")
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<CustomerDetailsDto> fetchCustomerDetails(@Pattern(regexp = "^$|[0-9]{10}") @RequestParam String mobileNumber) {

        log.info("fetchCustomerDetails {} start", mobileNumber);
        var customerDetails = this.customerService.fetchCustomerDetails(mobileNumber);
        log.info("fetchCustomerDetails {} end", mobileNumber);
        return ResponseEntity.ok(customerDetails);
    }
}
