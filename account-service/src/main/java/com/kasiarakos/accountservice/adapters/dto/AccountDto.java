package com.kasiarakos.accountservice.adapters.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class AccountDto {
  @NotEmpty
  @Pattern(regexp = "^$|[0-9]{10}")
  private Long accountNumber;

  @NotEmpty private String accountType;
  @NotEmpty private String branchAddress;
}
