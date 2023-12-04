package com.kanri.api.dto.account;


import com.kanri.api.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class IdentityProviderRecord {
    private String uid;

    private String email;

    private String displayName;
}
