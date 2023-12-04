package com.kanri.api.service.impl;

import com.google.firebase.auth.AuthErrorCode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.UserRecord.CreateRequest;
import com.kanri.api.dto.account.IdentityProviderRecord;
import com.kanri.api.entity.Role;
import com.kanri.api.exception.BadRequestException;
import com.kanri.api.exception.UnexpectedErrorException;
import com.kanri.api.service.IdentityProviderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class FirebaseIdentityProvider implements IdentityProviderService {
    private final FirebaseAuth auth;

    public IdentityProviderRecord createUserWithEmailAndPassword(String email, String password, String displayName) {
        CreateRequest request = new CreateRequest();
        request.setEmail(email);
        request.setPassword(password);
        request.setDisplayName(displayName);
        try {
            UserRecord user = auth.createUser(request);
            log.info("Successfully created firebase user. UID {}, email {}", user.getUid(), user.getEmail());
            return new IdentityProviderRecord(user.getUid(), user.getEmail(), user.getDisplayName());
        } catch (FirebaseAuthException exception) {
            log.error(exception.getLocalizedMessage());
            if (exception.getAuthErrorCode() == AuthErrorCode.EMAIL_ALREADY_EXISTS) {
                throw new BadRequestException("Email " + email + " is already in use");
            }
            throw new UnexpectedErrorException("An unexpected error occurred, please try again later.");
        }
    }
}
