package com.kanri.api.service;

import com.kanri.api.dto.account.IdentityProviderRecord;
import com.kanri.api.entity.Role;

public interface IdentityProviderService {
    IdentityProviderRecord createUserWithEmailAndPassword(String email, String password, String displayName);
}