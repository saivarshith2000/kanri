package com.kanri.api.dto.project;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateProjectRequest {
    @NotBlank
    @Size(min = 4, max = 32)
    private String name;

    @NotBlank
    @Pattern(regexp = "^[A-Z]{3,8}$", message = "Code must be UPPERCASE Alphabets and between 3 to 8 characters")
    private String code;

    @NotBlank
    @Size(max = 256)
    private String description;
}
