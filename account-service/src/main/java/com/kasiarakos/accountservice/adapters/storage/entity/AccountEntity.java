package com.kasiarakos.accountservice.adapters.storage.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "accounts")
public class AccountEntity extends BaseEntity{

  @Id
  private Long accountNumber;
  private Long customerId;
  private String accountType;
  private String branchAddress;
}
