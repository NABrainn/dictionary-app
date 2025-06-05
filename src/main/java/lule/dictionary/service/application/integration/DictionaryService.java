package lule.dictionary.service.application.integration;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lule.dictionary.dto.application.interfaces.imports.Import;
import lule.dictionary.dto.application.interfaces.translation.Translation;
import lule.dictionary.dto.application.interfaces.userProfile.UserProfileSettings;
import lule.dictionary.exception.RepositoryException;
import lule.dictionary.exception.ServiceException;
import lule.dictionary.factory.dto.ImportFactory;
import lule.dictionary.service.DocumentParsingService;
import lule.dictionary.service.StringParsingService;
import lule.dictionary.service.application.ImportData;
import lule.dictionary.service.application.entity.ImportService;
import lule.dictionary.service.application.entity.TranslationService;
import lule.dictionary.validation.implementation.UrlValidationStrategy;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DictionaryService {

    private final ImportService importService;
    private final TranslationService translationService;
    private final DocumentParsingService contentParser;
    private final StringParsingService stringParsingService;

    private final UrlValidationStrategy urlValidationStrategy;

    public ImportData loadImport(int importId) {
        try {
            Import imported = importService.findById(importId);
            String title = imported.importDetails().title();
            List<String> content = stringParsingService.toWhitespaceSplit(imported.importDetails().content());
            Map<String, Translation> translations = findByImport(imported);
            return new ImportData(title, content, translations, importId);
        } catch (ServiceException e) {
            throw new ServiceException(e.getMessage(), e.getCause());
        }
    }

    public int addImport(String title, String url, UserProfileSettings userProfileSettings, String owner) throws IOException {
        if(!url.startsWith("https://") || !url.startsWith("http://")) {
            String urlWithHttps = "https://".concat(url);
            boolean result = urlValidationStrategy.validate(urlWithHttps);
            if (result) {
                Document document = contentParser.fetchContent(urlWithHttps);
                String content = document.text();
                int importId = importService.addImport(ImportFactory.createImport(
                        ImportFactory.createImportDetails(
                                title,
                                content,
                                url
                        ),
                        userProfileSettings,
                        owner
                ));
                return importId;
            }
        }

        throw new ServiceException("Invalid url provided");
    }

    public List<Translation> findByTargetWords(List<String> targetWords) {
        try {
            List<String> validTargetWords = targetWords.stream()
                    .map(word -> stringParsingService.removeNonLetters(word).trim().toLowerCase())
                    .filter(word -> !word.isEmpty())
                    .distinct()
                    .toList();
            return translationService.findByTargetWords(validTargetWords);
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage(), e.getCause());
        }
    }

    private Map<String, Translation> findByImport(@NonNull Import imported) {
        try {
            List<String> targetWords = contentParser.parse(imported.importDetails().content());
            return findByTargetWords(targetWords).stream().collect(Collectors.toUnmodifiableMap((key) -> key.translationDetails().targetWord(), (value) -> value));

        } catch (ServiceException e) {
            throw new ServiceException(e.getMessage(), e.getCause());
        }
    }
}
