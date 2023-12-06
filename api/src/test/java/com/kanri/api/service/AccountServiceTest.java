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

import java.util.List;
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
        String name = "testuser";
        Account account = new Account(uid, email, name);
        when(accountRepository.findByUid(uid)).thenReturn(Optional.of(account));
        AccountDTO dto = accountService.getAccountByUid(uid);
        assertThat(dto.getUid()).isEqualTo(uid);
        assertThat(dto.getEmail()).isEqualTo(email);
        assertThat(dto.getDisplayName()).isEqualTo(name);
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
    @DisplayName("Get List of user accounts with email search string")
    void getAccountsWithEmailSearchString() {
        Account account = new Account("uid-test-a", "testa@testmail.com", "testuser");
        String emailSearch = "testa";
        when(accountRepository.findByEmailContainingIgnoreCase(emailSearch)).thenReturn(List.of(account));
        List<AccountDTO> accounts = accountService.searchByEmail(emailSearch);
        assertThat(accounts.size()).isEqualTo(1);
        assertThat(accounts.get(0).getEmail()).isEqualTo(account.getEmail());
        assertThat(accounts.get(0).getUid()).isEqualTo(account.getUid());
    }
}
