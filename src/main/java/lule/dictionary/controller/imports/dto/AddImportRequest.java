package lule.dictionary.controller.imports.dto;

import lule.dictionary.dto.application.interfaces.userProfile.UserProfileSettings;

public record AddImportRequest(String title, String url, UserProfileSettings userProfileSettings, String owner) {
}
