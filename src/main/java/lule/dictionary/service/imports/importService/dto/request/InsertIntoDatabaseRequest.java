package lule.dictionary.service.imports.importService.dto.request;

import lule.dictionary.dto.database.interfaces.userProfile.base.UserProfile;

public record InsertIntoDatabaseRequest(CreateImportRequest request, String content, UserProfile userProfile) {
    public static InsertIntoDatabaseRequest of(CreateImportRequest createImportRequest, String content, UserProfile userProfile) {
        return new InsertIntoDatabaseRequest(createImportRequest, content, userProfile);
    }
}
