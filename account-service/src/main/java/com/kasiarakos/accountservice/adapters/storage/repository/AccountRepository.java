package com.kasiarakos.accountservice.adapters.storage.repository;

import com.kasiarakos.accountservice.adapters.storage.entity.AccountEntity;
import java.util.Optional;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

  Optional<AccountEntity> findByCustomerId(Long customerId);

  @Transactional
  @Modifying
  void deleteByCustomerId(Long customerId);
}
