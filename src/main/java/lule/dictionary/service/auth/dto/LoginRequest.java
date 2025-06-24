package lule.dictionary.service.auth.dto;

import jakarta.validation.constraints.*;

public record LoginRequest(
        @NotBlank(message = "Login cannot be empty")
        @Size(min = 8, max = 50, message = "Login must be between 8 and 50 characters")
        @Pattern(
                regexp = "^[a-zA-Z]{8,50}$",
                message = "Login must contain only letters (A–Z, a–z)"
        )
        String login,
        @NotBlank(message = "Password cannot be empty")
        @Size(min = 8, max = 200, message = "Password must be between 8 and 200 characters")
        String password) {
}
