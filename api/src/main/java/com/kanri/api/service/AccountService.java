package com.kanri.api.service;

import com.kanri.api.dto.account.AccountDTO;
import com.kanri.api.entity.Account;
import com.kanri.api.exception.NotFoundException;
import com.kanri.api.mapper.AccountMapper;
import com.kanri.api.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountMapper mapper;

    public AccountDTO getAccountByUid(String uid) {
        return mapper.toAccountDTO(accountRepository
                .findByUid(uid)
                .orElseThrow(() -> new NotFoundException("Account with UID " + uid + " not found")));
    }

    public AccountDTO syncAccount(String uid, String email) {
        Optional<Account> accountInDb = accountRepository.findByUid(uid);
        if (accountInDb.isEmpty()) {
            Account account = accountRepository.save(new Account(uid, email));
            log.info("Created a new account with uid " + uid + " and email " + email);
            return mapper.toAccountDTO(account);
        }
        return mapper.toAccountDTO(accountInDb.get());
    }
}
