package lule.dictionary.service.imports.importService.dto.request;

public record LoadImportPageRequest(int wordId, int importId, int page) {
    public static LoadImportPageRequest of(int wordId, int importId, int page) {
        return new LoadImportPageRequest(wordId, importId, page);
    }
}
