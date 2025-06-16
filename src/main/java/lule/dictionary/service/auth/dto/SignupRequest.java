package lule.dictionary.service.auth.dto;

import lombok.NonNull;

public record SignupRequest(@NonNull String username, @NonNull String email, @NonNull String password) {
}
