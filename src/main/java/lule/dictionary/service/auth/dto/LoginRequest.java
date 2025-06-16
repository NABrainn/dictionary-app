package lule.dictionary.service.auth.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "Login cannot be empty")
        @Min(value = 8, message = "Login must be at least 8 characters")
        @Max(value = 50, message = "Login cannot have more than 50 characters")
        String login,

        @NotBlank(message = "Password cannot be empty")
        @Min(value = 8, message = "Password must be at least 8 characters")
        @Max(value = 200, message = "Password cannot have more than 200 characters")
        String password) {
}
