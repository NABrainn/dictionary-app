package lule.dictionary.controller.auth.dto;

import lombok.NonNull;

public record LoginRequest(@NonNull String login, @NonNull String password) {
}
