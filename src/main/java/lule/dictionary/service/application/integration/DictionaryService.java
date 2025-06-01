package lule.dictionary.service.application.integration;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lule.dictionary.dto.application.interfaces.imports.Import;
import lule.dictionary.dto.application.interfaces.translation.Translation;
import lule.dictionary.exception.ServiceException;
import lule.dictionary.service.ContentParsingService;
import lule.dictionary.service.application.dto.ImportService;
import lule.dictionary.service.application.dto.TranslationService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class DictionaryService {

    private final ImportService importService;
    private final TranslationService translationService;
    private final ContentParsingService stringParser;

    public List<Translation> loadImport(int importId) {
        try {
            Import imported = importService.findById(importId);
            return findByImport(imported);

        } catch (ServiceException e) {
            throw new ServiceException("Failed to load import");
        }
    }

    private List<Translation> findByImport(@NonNull Import imported) {
        try {
            List<String> targetWords = stringParser.parse(imported.importDetails().content());
            return translationService.findByTargetWords(targetWords);

        } catch (ServiceException e) {
            throw new ServiceException("Failed to load import");
        }
    }
}
