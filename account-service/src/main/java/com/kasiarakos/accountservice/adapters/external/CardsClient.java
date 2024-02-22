package com.kasiarakos.accountservice.adapters.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "card-service", fallback =  CardsFallback.class)
public interface CardsClient {

  @GetMapping(value = "/api/fetch", consumes = "application/json")
  ResponseEntity<CardsDto> fetchCardDetails(@RequestParam String mobileNumber);

}
