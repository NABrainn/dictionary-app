package lule.dictionary.service.dto.request.factory;

import lule.dictionary.entity.application.interfaces.imports.ImportWithPagination;
import lule.dictionary.entity.application.interfaces.userProfile.base.UserProfile;
import lule.dictionary.service.imports.dto.request.AssembleImportPageRequest;
import lule.dictionary.service.imports.dto.request.CreateImportRequest;
import lule.dictionary.service.imports.dto.request.InsertIntoDatabaseRequest;
import lule.dictionary.service.imports.dto.request.LoadImportPageRequest;

public interface AbstractServiceRequestFactory {
    CreateImportRequest newCreateImportRequest(String title, String content, String url, String owner);
    LoadImportPageRequest newLoadImportPageRequest(int wordId, int importId, int page);
    InsertIntoDatabaseRequest newInsertIntoDatabaseRequest(CreateImportRequest validRequest, String content, UserProfile userProfile);
    AssembleImportPageRequest newAssembleImportPageRequest(int wordId, int importId, int page, ImportWithPagination importWithPagination, int totalLength);
}
