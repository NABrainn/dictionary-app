package lule.dictionary.service.dto.request.factory;

import lule.dictionary.entity.application.interfaces.imports.ImportWithPagination;
import lule.dictionary.entity.application.interfaces.userProfile.base.UserProfile;
import lule.dictionary.service.imports.dto.request.AssembleImportPageRequest;
import lule.dictionary.service.imports.dto.request.CreateImportRequest;
import lule.dictionary.service.imports.dto.request.InsertIntoDatabaseRequest;
import lule.dictionary.service.imports.dto.request.LoadImportPageRequest;

public class AbstractServiceRequestFactoryImp implements AbstractServiceRequestFactory{
    @Override
    public CreateImportRequest newCreateImportRequest(String title, String content, String url, String owner) {
        return new CreateImportRequest(title, content, url, owner);
    }

    @Override
    public LoadImportPageRequest newLoadImportPageRequest(int wordId, int importId, int page) {
        return new LoadImportPageRequest(wordId, importId, page);
    }

    @Override
    public InsertIntoDatabaseRequest newInsertIntoDatabaseRequest(CreateImportRequest validRequest, String content, UserProfile userProfile) {
        return new InsertIntoDatabaseRequest(validRequest, content, userProfile);
    }

    @Override
    public AssembleImportPageRequest newAssembleImportPageRequest(int wordId, int importId, int page, ImportWithPagination importWithPagination, int totalLength) {
        return new AssembleImportPageRequest(wordId, importId, page, importWithPagination, totalLength);
    }
}
