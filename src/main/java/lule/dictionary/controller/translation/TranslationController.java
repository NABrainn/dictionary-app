package lule.dictionary.controller.translation;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import lule.dictionary.controller.translation.dto.AddTranslationRequest;
import lule.dictionary.controller.translation.dto.FindTranslationRequest;
import lule.dictionary.controller.translation.dto.UpdateFamiliarityRequest;
import lule.dictionary.enumeration.Familiarity;
import lule.dictionary.enumeration.Language;
import lule.dictionary.service.translation.TranslationService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping({"/translations", "/translations/"})
@Slf4j
public class TranslationController {

    private final TranslationService translationService;

    @GetMapping({"/", ""})
    public String findByTargetWord(Model model,
                                   @RequestParam int importId,
                                   @RequestParam String targetWord,
                                   @RequestParam("selectedWordId") int selectedWordId) {
            boolean found = translationService.findByTargetWord(model, new FindTranslationRequest(importId, targetWord, selectedWordId));
            if(found) {
                return "import-page/translation/update-translation-form";
            }
            return "import-page/translation/add-translation-form";
    }

    @PostMapping({"/new", "new"})
    public String newTranslation(RedirectAttributes redirectAttributes,
                                 Authentication authentication,
                                 @RequestParam("sourceWord") String sourceWord,
                                 @RequestParam("targetWord") String targetWord,
                                 @RequestParam("familiarity") Familiarity familiarity,
                                 @RequestParam("sourceLanguage") Language sourceLanguage,
                                 @RequestParam("targetLanguage") Language targetLanguage,
                                 @RequestParam("importId") int importId,
                                 @RequestParam("selectedWordId") int selectedWordId) {
        translationService.add(redirectAttributes, new AddTranslationRequest(sourceWord, targetWord, familiarity, sourceLanguage, targetLanguage, authentication.getName(), importId, selectedWordId));
        return "redirect:/catalog/page/reload";
    }

    @PutMapping({"/familiarity/update", "familiarity/update"})
    public String updateFamiliarity(RedirectAttributes redirectAttributes,
                                    Authentication authentication,
                                    @RequestParam("sourceWord") String sourceWord,
                                    @RequestParam("targetWord") String targetWord,
                                    @RequestParam("familiarity") Familiarity familiarity,
                                    @RequestParam("sourceLanguage") Language sourceLanguage,
                                    @RequestParam("targetLanguage") Language targetLanguage,
                                    @RequestParam("importId") int importId,
                                    @RequestParam("selectedWordId") int selectedWordId) {
        translationService.updateFamiliarity(redirectAttributes, new UpdateFamiliarityRequest(targetWord, familiarity, sourceWord, sourceLanguage, targetLanguage, authentication.getName(), importId, selectedWordId));
        return "redirect:/catalog/page/reload";
    }
}
