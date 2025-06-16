package lule.dictionary.service.auth.dto;

import lombok.NonNull;

public record LoginRequest(@NonNull String login, @NonNull String password) {
}
