package com.kasiarakos.accountservice.adapters.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(name = "Customer",
        description = "Schema to hold customer and Account information")
public class CustomerDto {

  @NotEmpty(message = "Name cannot be empty")
  @Size(min = 5, max = 30)
  @Schema(example = "Eazy Bytes")
  private String name;

  @Email
  @NotEmpty(message = "Email address cannot be empty")
  @Schema(example = "test@gmail.com")
  private String email;

  @Pattern(regexp = "^$|[0-9]{10}")
  private String mobileNumber;
  private AccountDto account;
}
