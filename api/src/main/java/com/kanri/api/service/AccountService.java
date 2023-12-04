package com.kanri.api.service;

import com.kanri.api.dto.account.AccountDTO;
import com.kanri.api.dto.account.IdentityProviderRecord;
import com.kanri.api.dto.account.RegisterRequest;
import com.kanri.api.entity.Account;
import com.kanri.api.exception.NotFoundException;
import com.kanri.api.mapper.AccountMapper;
import com.kanri.api.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountService {
    private final IdentityProviderService identityProviderService;
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
     * Attempts to register an account using the identity provider's email and password method
     * If the attempt is successful, it inserts an entry in DB and returns the user information
     * @param dto   Register Request DTO containing the required user information
     * @return      Account details stored in the database
     */    public AccountDTO createUser(RegisterRequest dto) {
        log.info("Creating Firebase User for {}", dto.getEmail());
        IdentityProviderRecord record = identityProviderService.createUserWithEmailAndPassword(dto.getEmail(), dto.getPassword(), dto.getDisplayName());
        Account account = new Account();
        account.setUid(record.getUid());
        account.setEmail(record.getEmail());
        account.setDisplayName(record.getDisplayName());
        return mapper.toAccountDTO(accountRepository.save(account));
    }

    /**
     * Returns a list of AccountDTOs with emails matching the provider seach string
     * @param emailSearch String used to search accounts based on their email
     * @return A list of accountDTO with matching email ids
     */
    public List<AccountDTO> searchByEmail(String emailSearch) {
        return accountRepository.findByEmailContainingIgnoreCase(emailSearch)
                .stream()
                .map(mapper::toAccountDTO)
                .collect(Collectors.toList());
    }
}
