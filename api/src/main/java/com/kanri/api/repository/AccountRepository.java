package com.kanri.api.repository;

import com.kanri.api.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByUid(String uid);
    Optional<Account> findByUid(String uid);
    Optional<Account> findByEmail(String email);
    List<Account> findByEmailContainingIgnoreCase(String emailSearch);
}
