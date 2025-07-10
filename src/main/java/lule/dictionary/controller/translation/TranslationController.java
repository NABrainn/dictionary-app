package lule.dictionary.controller.translation;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import lule.dictionary.entity.application.interfaces.userProfile.CustomUserDetails;
import lule.dictionary.exception.RetryViewException;
import lule.dictionary.service.dto.result.ServiceResult;
import lule.dictionary.service.translation.dto.*;
import lule.dictionary.enumeration.Familiarity;
import lule.dictionary.service.language.Language;
import lule.dictionary.service.translation.TranslationServiceImp;
import lule.dictionary.service.translation.dto.request.AddTranslationRequest;
import lule.dictionary.service.translation.exception.SourceWordNotFoundException;
import lule.dictionary.service.translation.exception.TranslationNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/translations")
@Slf4j
public class TranslationController {

    private final TranslationServiceImp translationService;

    @GetMapping("")
    public String findByTargetWord(Model model,
                                   Authentication authentication,
                                   @RequestParam int importId,
                                   @RequestParam String targetWord,
                                   @RequestParam("selectedWordId") int selectedWordId,
                                   @RequestParam("page") int page) {
        try {
            CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
            translationService.findByTargetWord(model, new FindTranslationRequest(importId, targetWord, selectedWordId, page),
                    principal.getUsername(),
                    principal.sourceLanguage(),
                    principal.targetLanguage());
            return "import-page/translation/update-translation-form";
        } catch (RetryViewException e) {
            log.warn("Retrying view due to input issue: {}", e.getMessage());
            return "import-page/translation/update-translation-form";
        } catch (TranslationNotFoundException e) {
            log.info("Translation not found for '{}', showing creation form. Details: {}", targetWord, e.getMessage());
            return "import-page/translation/add-translation-form";
        }
    }

    @PostMapping({"/new", "/new/"})
    public String newTranslation(Model model,
                                 Authentication authentication,
                                 @RequestParam("sourceWords") List<String> sourceWords,
                                 @RequestParam("targetWord") String targetWord,
                                 @RequestParam("familiarity") Familiarity familiarity,
                                 @RequestParam("sourceLanguage") Language sourceLanguage,
                                 @RequestParam("targetLanguage") Language targetLanguage,
                                 @RequestParam("importId") int importId,
                                 @RequestParam("selectedWordId") int selectedWordId,
                                 @RequestParam("page") int page) {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        ServiceResult<TranslationAttribute> result = translationService.createTranslation(AddTranslationRequest.builder()
                .importId(importId)
                .selectedWordId(selectedWordId)
                .sourceWords(sourceWords)
                .targetWord(targetWord)
                .sourceLanguage(sourceLanguage)
                .targetLanguage(targetLanguage)
                .familiarity(familiarity)
                .page(page)
                .owner(principal.getUsername())
                .build());
        model.addAttribute("translationAttribute", result.value());
        return "forward:/imports/page/reload";
    }

    @PutMapping({"/familiarity/update", "/familiarity/update/"})
    public String updateFamiliarity(Model model,
                                    Authentication authentication,
                                    @RequestParam("targetWord") String targetWord,
                                    @RequestParam("familiarity") Familiarity familiarity,
                                    @RequestParam("sourceLanguage") Language sourceLanguage,
                                    @RequestParam("targetLanguage") Language targetLanguage,
                                    @RequestParam("importId") int importId,
                                    @RequestParam("selectedWordId") int selectedWordId,
                                    @RequestParam("page") int page) {
        try {
            translationService.updateFamiliarity(model, new UpdateTranslationFamiliarityRequest(
                    targetWord,
                    familiarity,
                    sourceLanguage,
                    targetLanguage,
                    authentication.getName(),
                    importId,
                    selectedWordId,
                    page
            ));
            return "forward:/imports/page/reload";
        } catch (TranslationNotFoundException e) {
            log.info("Sending to isError page due to translation not found: {}", e.getMessage());
            return "isError";
        }
    }

    @PutMapping({"/sourceWords/update", "/sourceWords/update/"})
    public String updateSourceWords(Model model,
                                    Authentication authentication,
                                    @RequestParam("sourceWords") List<String> sourceWords,
                                    @RequestParam("targetWord") String targetWord) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        try {
            translationService.updateSourceWords(model, new UpdateSourceWordsRequest(
                    sourceWords,
                    targetWord,
                    userDetails.getUsername()
            ));
            return "import-page/translation/update-source-words-form";
        } catch (RetryViewException e) {
            log.warn("Retrying view due to input issue: {}", e.getMessage());
            return "import-page/translation/update-source-words-form";
        }
    }

    @DeleteMapping({"/sourceWords/delete", "/sourceWords/delete/"})
    public String deleteSourceWord(Model model,
                                   Authentication authentication,
                                   @RequestParam("sourceWord") String sourceWord,
                                   @RequestParam("targetWord") String targetWord) {
        try {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            translationService.deleteSourceWord(model, new DeleteSourceWordRequest(
                    sourceWord,
                    targetWord,
                    userDetails.getUsername()
            ));
            return "import-page/translation/source-words-list";
        } catch (RetryViewException | SourceWordNotFoundException e) {
            log.warn("Retrying view due to input issue: {}", e.getMessage());
            return "import-page/translation/source-words-list";
        }
    }
}
