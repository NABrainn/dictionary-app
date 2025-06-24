package lule.dictionary.service.imports.importPageService;

import lombok.RequiredArgsConstructor;
import lule.dictionary.service.imports.importPageService.dto.ImportPageModel;
import lule.dictionary.service.imports.importPageService.dto.SaveTranslationRequest;
import lule.dictionary.entity.application.interfaces.imports.base.Import;
import lule.dictionary.entity.application.interfaces.translation.Translation;
import lule.dictionary.exception.ServiceException;
import lule.dictionary.service.imports.importService.ImportService;
import lule.dictionary.service.translation.TranslationService;
import lule.dictionary.service.translation.dto.TranslationModel;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ImportPageService {

    private final ImportService importService;
    private final TranslationService translationService;

    public void loadImportWithTranslations(Model model, SaveTranslationRequest saveTranslationRequest) {
        Import imported = importService.findById(saveTranslationRequest.importId());
        String title = imported.title();
        List<String> content = List.of(imported.content().split("[ \\n]+"));
        Map<String, Translation> translations = translationService.findTranslationsByImport(imported);
        model.addAttribute("importPageModel", new ImportPageModel(title, content, translations, saveTranslationRequest.importId(), saveTranslationRequest.wordId()));
        try {
            model.addAttribute("translationModel", saveTranslationRequest.translationModel());
        } catch (NullPointerException e) {
            model.addAttribute("translationModel", null);
        }
    }
}
