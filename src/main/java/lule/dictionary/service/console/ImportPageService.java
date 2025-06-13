package lule.dictionary.service.console;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lule.dictionary.controller.catalog.dto.ImportPageModel;
import lule.dictionary.controller.catalog.dto.SaveTranslationRequest;
import lule.dictionary.dto.application.interfaces.imports.Import;
import lule.dictionary.dto.application.interfaces.translation.Translation;
import lule.dictionary.exception.ServiceException;
import lule.dictionary.service.StringParsingService;
import lule.dictionary.service.imports.ImportService;
import lule.dictionary.service.translation.TranslationService;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ImportPageService {

    private final ImportService importService;
    private final TranslationService translationService;
    private final StringParsingService stringParsingService;

    public void loadImportWithTranslations(Model model, SaveTranslationRequest saveTranslationRequest) {
        try {
            Import imported = importService.findById(saveTranslationRequest.importId());
            String title = imported.importDetails().title();
            List<String> content = stringParsingService.toWhitespaceSplit(imported.importDetails().content());
            Map<String, Translation> translations = translationService.findTranslationsByImport(imported);
            model.addAttribute("importPageModel", new ImportPageModel(title, content, translations, saveTranslationRequest.importId(), saveTranslationRequest.wordId()));
        } catch (ServiceException e) {
            throw new ServiceException(e.getMessage(), e.getCause());
        }
    }
}
