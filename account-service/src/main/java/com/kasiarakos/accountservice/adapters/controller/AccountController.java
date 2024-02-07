package com.kasiarakos.accountservice.adapters.controller;

import static com.kasiarakos.accountservice.adapters.controller.constants.AccountConstants.MESSAGE_200;
import static com.kasiarakos.accountservice.adapters.controller.constants.AccountConstants.MESSAGE_201;
import static com.kasiarakos.accountservice.adapters.controller.constants.AccountConstants.MESSAGE_500;
import static com.kasiarakos.accountservice.adapters.controller.constants.AccountConstants.STATUS_200;
import static com.kasiarakos.accountservice.adapters.controller.constants.AccountConstants.STATUS_201;
import static com.kasiarakos.accountservice.adapters.controller.constants.AccountConstants.STATUS_417;

import com.kasiarakos.accountservice.adapters.dto.AccountsContactInfoDto;
import com.kasiarakos.accountservice.adapters.dto.CustomerDto;
import com.kasiarakos.accountservice.adapters.dto.ErrorResponseDto;
import com.kasiarakos.accountservice.adapters.dto.ResponseDto;
import com.kasiarakos.accountservice.domain.service.AccountService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "CRUD REST api for accounts", description = "crud")
@RestController
@RequestMapping(path = "/api/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class AccountController {

  private final AccountService accountService;
  private final String buildVersion;
  private final Environment environment;
  private final AccountsContactInfoDto accountsContactInfoDto;

  public AccountController(AccountService accountService,
                           @Value("${build.version}") String buildVersion,
                           Environment environment,
                           AccountsContactInfoDto accountsContactInfoDto) {
    this.accountService = accountService;
    this.buildVersion = buildVersion;
    this.environment = environment;
    this.accountsContactInfoDto = accountsContactInfoDto;
  }

  @ApiResponses({
    @ApiResponse(responseCode = "201", description = "Http status CREATED"),
    @ApiResponse(
        responseCode = "500",
        description = "Http status Internal server error",
        content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
  })
  @Operation(summary = "Create Account rest api", description = "Creates accounts")
  @PostMapping
  public ResponseEntity<ResponseDto> createAccount(@Valid @RequestBody CustomerDto customerDto) {
    this.accountService.createAccount(customerDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto(STATUS_201, MESSAGE_201));
  }

  @Operation(summary = "Fetching Account rest api", description = "Fetches accounts")
  @GetMapping
  public ResponseEntity<CustomerDto> fetchAccountDetails(
      @Pattern(regexp = "^$|[0-9]{10}") @RequestParam String mobileNumber) {

    var customerDto = this.accountService.fetchCustomerByMobileNumber(mobileNumber);
    return ResponseEntity.status(HttpStatus.OK).body(customerDto);
  }

  @Operation(summary = "Updating Account rest api", description = "Updates accounts")
  @PutMapping
  public ResponseEntity<ResponseDto> updateAccount(@Valid @RequestBody CustomerDto customerDto) {
    var updated = this.accountService.updateAccount(customerDto);
    if (updated) {
      return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(STATUS_200, MESSAGE_200));
    } else {
      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
          .body(new ResponseDto(STATUS_417, MESSAGE_500));
    }
  }

  @DeleteMapping
  public ResponseEntity<ResponseDto> deleteAccountDetails(
      @Pattern(regexp = "^$|[0-9]{10}") @RequestParam String mobileNumber) {
    var deleted = this.accountService.deleteAccount(mobileNumber);
    if (deleted) {
      return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(STATUS_200, MESSAGE_200));
    } else {
      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
          .body(new ResponseDto(STATUS_417, MESSAGE_500));
    }
  }
  @Retry(name = "getBuildInfo", fallbackMethod = "getBuildVersionFallback")
  @GetMapping("build-info")
  public String getBuildVersion() {
    return this.buildVersion;
  }

  public String getBuildVersionFallback(Throwable throwable) {
    return "0.9";
  }

  @RateLimiter(name = "getJavaVersion", fallbackMethod = "getJavaVersionFallback")
  @GetMapping("java-version")
  public String getJavaVersion() {
    return this.environment.getProperty("JAVA_HOME");
  }

  public String getJavaVersionFallback(Throwable throwable) {
    return "JAVA 17";
  }

  @GetMapping("contact-info")
  public AccountsContactInfoDto getContactInfo() {
    return accountsContactInfoDto;
  }
}
