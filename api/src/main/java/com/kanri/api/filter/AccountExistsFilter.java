package com.kanri.api.filter;

import com.kanri.api.entity.Account;
import com.kanri.api.exception.ForbiddenException;
import com.kanri.api.repository.AccountRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AccountExistsFilter extends OncePerRequestFilter {
    private final AccountRepository accountRepository;

    @Override
    public void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws IOException, ServletException {
        String path = request.getServletPath();
        if (path.equals("/api/accounts/sync")) {
            // Do not invoke this filter for account sync path
            filterChain.doFilter(request, response);
            return;
        }
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Account> account = accountRepository.findByUid(jwt.getSubject());
        if (account.isPresent()) {
            filterChain.doFilter(request, response);
            return;
        }
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.getOutputStream().print("An account with UID " + jwt.getSubject() + " doesn't exist");
    }
}
