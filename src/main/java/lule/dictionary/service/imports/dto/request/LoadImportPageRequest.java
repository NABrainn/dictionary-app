package lule.dictionary.service.imports.dto.request;

import lule.dictionary.service.dto.request.ServiceRequest;

public record LoadImportPageRequest(int wordId, int importId, int page) implements ServiceRequest {
}
