package lule.dictionary.controller.translation;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import lule.dictionary.controller.translation.dto.AddTranslationRequest;
import lule.dictionary.controller.translation.dto.FindTranslationRequest;
import lule.dictionary.controller.translation.dto.UpdateFamiliarityRequest;
import lule.dictionary.enumeration.Familiarity;
import lule.dictionary.enumeration.Language;
import lule.dictionary.service.translation.TranslationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
                return "translation/update-translation-form";
            }
            return "translation/add-translation-form";
    }

    @PostMapping({"/new", "new"})
    public String newTranslation(Model model,
                                @RequestParam("sourceWord") String sourceWord,
                                @RequestParam("targetWord") String targetWord,
                                @RequestParam("familiarity") Familiarity familiarity,
                                @RequestParam("sourceLanguage") Language sourceLanguage,
                                @RequestParam("targetLanguage") Language targetLanguage,
                                @RequestParam("owner") String owner,
                                @RequestParam("importId") int importId,
                                @RequestParam("selectedWordId") int selectedWordId) {
        translationService.add(model, new AddTranslationRequest(sourceWord, targetWord, familiarity, sourceLanguage, targetLanguage, owner, importId, selectedWordId));
        return "forward:/catalog/page/reload";
    }

    @PutMapping({"/familiarity/update", "familiarity/update"})
    public String updateFamiliarity(Model model,
                                    @RequestParam("sourceWord") String sourceWord,
                                    @RequestParam("targetWord") String targetWord,
                                    @RequestParam("familiarity") Familiarity familiarity,
                                    @RequestParam("sourceLanguage") Language sourceLanguage,
                                    @RequestParam("targetLanguage") Language targetLanguage,
                                    @RequestParam("owner") String owner,
                                    @RequestParam("importId") int importId,
                                    @RequestParam("selectedWordId") int selectedWordId) {

        translationService.updateFamiliarity(model, new UpdateFamiliarityRequest(targetWord, familiarity, sourceWord, sourceLanguage, targetLanguage, owner, importId, selectedWordId));
        return "forward:/catalog/page/reload";
    }
}
