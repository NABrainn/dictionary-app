package lule.dictionary.service.imports.importService.dto.insertIntoDatabaseRequest;

import lule.dictionary.entity.application.interfaces.userProfile.base.UserProfile;
import lule.dictionary.service.imports.importService.dto.createImportRequest.CreateImportRequest;
import org.springframework.stereotype.Service;

@Service
public class InsertIntoDatabaseRequestFactory {

    public InsertIntoDatabaseRequest of(CreateImportRequest validRequest, String content, UserProfile userProfile) {
        return new InsertIntoDatabaseRequest(validRequest, content, userProfile);
    }
}
