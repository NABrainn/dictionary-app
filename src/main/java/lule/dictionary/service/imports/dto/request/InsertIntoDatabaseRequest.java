package lule.dictionary.service.imports.dto.request;

import lule.dictionary.entity.application.interfaces.userProfile.base.UserProfile;
import lule.dictionary.service.dto.request.ServiceRequest;

public record InsertIntoDatabaseRequest(CreateImportRequest validRequest, String content, UserProfile userProfile) implements ServiceRequest {
}
