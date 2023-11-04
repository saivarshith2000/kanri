package com.kanri.api.web;

import com.kanri.api.dto.account.AccountDTO;
import com.kanri.api.entity.Account;
import com.kanri.api.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @GetMapping("/me")
    public AccountDTO getMyAccount(@AuthenticationPrincipal Jwt jwt) {
        return accountService.getAccountByUid(jwt.getSubject());
    }

    @PostMapping("/sync")
    public AccountDTO syncAccount(@AuthenticationPrincipal Jwt jwt) {
        String uid = jwt.getSubject();
        String email = jwt.getClaimAsString("email");
        return accountService.syncAccount(uid, email);
    }
}
