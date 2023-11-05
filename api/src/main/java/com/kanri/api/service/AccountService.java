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

    /**
     * Returns user details that exist in kanri database
     * @param uid   Firebase UID of the user
     * @return      Account details
     */
    public AccountDTO getAccountByUid(String uid) {
        return mapper.toAccountDTO(accountRepository
                .findByUid(uid)
                .orElseThrow(() -> new NotFoundException("Account with UID " + uid + " not found")));
    }

    /**
     * Returns account details that are either newly created as a result of this request (or)
     * details that already existed previously. This endpoint is expected to be called by the
     * client on startup as there is no (simple) way to synchronize user records between kanri
     * database and firebase's backend.
     * @param uid   Firebase UID of the user
     * @param email User's email address as registered in Firebase
     * @return      Account details stored in the database
     */
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
