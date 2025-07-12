package lule.dictionary.service.imports.importService.dto.insertIntoDatabaseRequest;

import lule.dictionary.entity.application.interfaces.userProfile.base.UserProfile;
import lule.dictionary.service.imports.importService.dto.createImportRequest.CreateImportRequest;

public record InsertIntoDatabaseRequest(CreateImportRequest validRequest, String content, UserProfile userProfile) {
    public static InsertIntoDatabaseRequest of(CreateImportRequest validRequest, String content, UserProfile userProfile) {
        return new InsertIntoDatabaseRequest(validRequest, content, userProfile);
    }
}
