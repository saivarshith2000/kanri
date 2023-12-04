package com.kanri.api.web;

import com.kanri.api.dto.account.AccountDTO;
import com.kanri.api.dto.account.RegisterRequest;
import com.kanri.api.entity.Account;
import com.kanri.api.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @GetMapping("/me")
    public AccountDTO getMyAccount(@AuthenticationPrincipal Jwt jwt) {
        return accountService.getAccountByUid(jwt.getSubject());
    }

    @PostMapping("/register")
    public AccountDTO register(@RequestBody @Valid RegisterRequest dto) {
        return accountService.createUser(dto);
    }

    @GetMapping("/search")
    public List<AccountDTO> searchAccountsByEmail(@RequestParam(name = "email") String emailSearchString) {
        return accountService.searchByEmail(emailSearchString);
    }
}
