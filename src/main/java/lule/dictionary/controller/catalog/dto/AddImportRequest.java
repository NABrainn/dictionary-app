package lule.dictionary.controller.catalog.dto;

import lombok.NonNull;
import lule.dictionary.dto.application.interfaces.userProfile.UserProfileSettings;

public record AddImportRequest(@NonNull String title, @NonNull String url, @NonNull UserProfileSettings userProfileSettings, @NonNull String owner) {
}
