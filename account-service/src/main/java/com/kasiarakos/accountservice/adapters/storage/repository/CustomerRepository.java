package com.kasiarakos.accountservice.adapters.storage.repository;

import com.kasiarakos.accountservice.adapters.storage.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {
  Optional<CustomerEntity> findByMobileNumber(String mobileNumber);
}
