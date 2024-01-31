package com.kasiarakos.accountservice.adapters.external;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class CardsDto {
  private String mobileNumber;
  private String cardNumber;
  private String cardType;
  private int totalLimit;
  private int amountUsed;
  private int availableAmount;
}
