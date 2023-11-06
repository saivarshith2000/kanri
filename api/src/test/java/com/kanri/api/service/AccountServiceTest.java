package com.kanri.api.service;

import com.kanri.api.dto.account.AccountDTO;
import com.kanri.api.entity.Account;
import com.kanri.api.exception.NotFoundException;
import com.kanri.api.mapper.AccountMapper;
import com.kanri.api.repository.AccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;
    @Spy
    private AccountMapper mapper = Mappers.getMapper(AccountMapper.class);
    @InjectMocks
    private AccountService accountService;

    @Test()
    @DisplayName("Get Account by UID")
    void getAccountByUid() {
        String uid = "test-uid-abc-123";
        String email = "testuser@test.com";
        Account account = new Account();
        account.setUid(uid);
        account.setEmail(email);

        when(accountRepository.findByUid(uid)).thenReturn(Optional.of(account));

        AccountDTO dto = accountService.getAccountByUid(uid);

        assertThat(dto.getUid()).isEqualTo(uid);
        assertThat(dto.getEmail()).isEqualTo(email);
    }

    @Test
    @DisplayName("Throw NotFoundException is UID doesn't exist")
    void throwIfUidDoesntExist() {
        String uid = "invalid-uid";

        when(accountRepository.findByUid(uid)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NotFoundException.class, () -> {
            AccountDTO dto = accountService.getAccountByUid(uid);
        });
    }

    @Test
    @DisplayName("Sync Account when account already exists")
    void syncAccountForExistingAccount() {
        String uid = "test-uid-abc-123";
        String email = "testuser@test.com";
        Account account = new Account();
        account.setUid(uid);
        account.setEmail(email);

        when(accountRepository.findByUid(uid)).thenReturn(Optional.of(account));

        AccountDTO dto = accountService.syncAccount(uid, email);

        assertThat(dto.getUid()).isEqualTo(uid);
        assertThat(dto.getEmail()).isEqualTo(email);
    }

    @Test
    @DisplayName("Sync Account for a new account")
    void syncAccountForNewAccount() {
        String uid = "test-uid-abc-123";
        String email = "testuser@test.com";
        Account account = new Account();
        account.setUid(uid);
        account.setEmail(email);

        when(accountRepository.findByUid(uid)).thenReturn(Optional.empty());
        when(accountRepository.save(any(Account.class))).then(returnsFirstArg());

        AccountDTO dto = accountService.syncAccount(uid, email);

        assertThat(dto.getUid()).isEqualTo(uid);
        assertThat(dto.getEmail()).isEqualTo(email);
    }
}
