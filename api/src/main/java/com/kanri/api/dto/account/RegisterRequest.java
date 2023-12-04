package com.kanri.api.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NotNull
    @Email
    private String email;

    @NotNull
    @Length(min = 6, max = 64, message = "Password must be between 6 and 64 characters")
    private String password;

    @JsonProperty("display_name")
    @NotNull
    @Length(min = 3, max = 32, message = "display_name must be between 3 and 32 characters")
    private String displayName;
}