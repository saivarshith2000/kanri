package com.kanri.api.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AccountDTO {
    private String uid;

    private String email;

    @JsonProperty("display_name")
    private String displayName;
}
