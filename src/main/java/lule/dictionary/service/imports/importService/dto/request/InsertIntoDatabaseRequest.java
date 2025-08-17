package lule.dictionary.service.imports.importService.dto.request;

import lule.dictionary.dto.database.interfaces.userProfile.base.UserProfile;

public record InsertIntoDatabaseRequest(CreateDocumentRequest request, String content, UserProfile userProfile) {
    public static InsertIntoDatabaseRequest of(CreateDocumentRequest createImportRequest, String content, UserProfile userProfile) {
        return new InsertIntoDatabaseRequest(createImportRequest, content, userProfile);
    }
}
