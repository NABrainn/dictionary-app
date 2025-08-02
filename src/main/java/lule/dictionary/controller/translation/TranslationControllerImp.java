package lule.dictionary.controller.translation;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import lule.dictionary.dto.database.interfaces.userProfile.CustomUserDetails;
import lule.dictionary.exception.application.InvalidInputException;
import lule.dictionary.dto.application.result.ServiceResult;
import lule.dictionary.enumeration.Familiarity;
import lule.dictionary.service.language.Language;
import lule.dictionary.service.localization.LocalizationService;
import lule.dictionary.service.translation.TranslationServiceImp;
import lule.dictionary.service.translation.dto.attribute.TranslationAttribute;
import lule.dictionary.service.translation.dto.request.*;
import lule.dictionary.service.translation.exception.TranslationNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/translations")
@Slf4j
public class TranslationControllerImp {

    private final TranslationServiceImp translationService;
    private final LocalizationService localizationService;

    @GetMapping("")
    public String findByTargetWord(Model model,
                                   Authentication authentication,
                                   @RequestParam int documentId,
                                   @RequestParam String targetWord,
                                   @RequestParam("selectedWordId") int selectedWordId) {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        try {
            FindByTargetWordRequest request = FindByTargetWordRequest.builder()
                    .documentId(documentId)
                    .selectedWordId(selectedWordId)
                    .targetWord(targetWord)
                    .sourceLanguage(principal.sourceLanguage())
                    .targetLanguage(principal.targetLanguage())
                    .owner(principal.getUsername())
                    .build();
            ServiceResult<TranslationAttribute> result = translationService.findByTargetWord(request);
            model.addAttribute("translationAttribute", result.value());
            model.addAttribute("translationLocalization", localizationService.translationFormLocalization(principal.sourceLanguage()));
            return "document-page/content/translation/update/update-translation-form";

        } catch (TranslationNotFoundException e) {
            log.info("Translation not found, sending add-translation-form template: {}", e.getResult().value());
            model.addAttribute("translationAttribute", e.getResult().value());
            model.addAttribute("translationLocalization", localizationService.translationFormLocalization(principal.sourceLanguage()));
            return "document-page/content/translation/add/add-translation-form";

        } catch (InvalidInputException e) {
            log.info("Invalid input, sending info back");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid target word");
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
                                 @RequestParam("documentId") int documentId,
                                 @RequestParam("selectedWordId") int selectedWordId) {
        try {
            CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
            AddTranslationRequest request = AddTranslationRequest.builder()
                    .documentId(documentId)
                    .selectedWordId(selectedWordId)
                    .sourceWords(sourceWords)
                    .targetWord(targetWord)
                    .sourceLanguage(sourceLanguage)
                    .targetLanguage(targetLanguage)
                    .familiarity(familiarity)
                    .owner(principal.getUsername())
                    .build();
            ServiceResult<TranslationAttribute> result = createTranslation(request);
            model.addAttribute("translationAttribute", result.value());
            model.addAttribute("translationLocalization", localizationService.translationFormLocalization(principal.sourceLanguage()));
            return "document-page/content/translation/update/update-translation-form";
        } catch (InvalidInputException e) {
            String exceptionMessage = "Failed to add translation due to invalid input.";
            log.info(exceptionMessage);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exceptionMessage);
        }
    }

    @PutMapping({"/familiarity/update", "/familiarity/update/"})
    public String updateFamiliarity(Model model,
                                    Authentication authentication,
                                    @RequestParam("targetWord") String targetWord,
                                    @RequestParam("familiarity") Familiarity familiarity,
                                    @RequestParam("sourceLanguage") Language sourceLanguage,
                                    @RequestParam("targetLanguage") Language targetLanguage,
                                    @RequestParam("selectedWordId") int selectedWordId) {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        UpdateTranslationFamiliarityRequest request = UpdateTranslationFamiliarityRequest.builder()
                .selectedWordId(selectedWordId)
                .targetWord(targetWord)
                .sourceLanguage(sourceLanguage)
                .targetLanguage(targetLanguage)
                .familiarity(familiarity)
                .owner(principal.getUsername())
                .build();
        ServiceResult<TranslationAttribute> result = translationService.updateFamiliarity(request);
        model.addAttribute("translationAttribute", result.value());
        model.addAttribute("translationLocalization", localizationService.translationFormLocalization(principal.sourceLanguage()));
        return "document-page/content/translation/update/update-translation-form";
    }

    @PutMapping({"/sourceWords/update", "/sourceWords/update/"})
    public String updateSourceWords(Model model,
                                    Authentication authentication,
                                    @RequestParam("sourceWords") List<String> sourceWords,
                                    @RequestParam("targetWord") String targetWord,
                                    @RequestParam("familiarity") String currentFamiliarity,
                                    @RequestParam("selectedWordId") int selectedWordId) {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        try {
            UpdateSourceWordsRequest request = UpdateSourceWordsRequest.builder()
                    .sourceWords(sourceWords)
                    .familiarity(Familiarity.valueOf(currentFamiliarity.toUpperCase()))
                    .sourceLanguage(principal.sourceLanguage())
                    .targetLanguage(principal.targetLanguage())
                    .owner(principal.getUsername())
                    .targetWord(targetWord)
                    .selectedWordId(selectedWordId)
                    .build();
            ServiceResult<TranslationAttribute> result = translationService.updateSourceWords(request);
            model.addAttribute("translationAttribute", result.value());
            model.addAttribute("translationLocalization", localizationService.translationFormLocalization(principal.sourceLanguage()));
            model.addAttribute("hasError", result.hasError());
            return "document-page/content/translation/update/update-translation-form";
        } catch (InvalidInputException e) {
            log.warn("Invalid input: returning back");
            model.addAttribute("translationAttribute", e.getResult().value());
            model.addAttribute("translationLocalization", localizationService.translationFormLocalization(principal.sourceLanguage()));
            return "document-page/content/translation/update/update-translation-form";
        }
    }

    @DeleteMapping({"/sourceWords/delete", "/sourceWords/delete/"})
    public String deleteSourceWord(Model model,
                                   Authentication authentication,
                                   @RequestParam("sourceWord") String sourceWord,
                                   @RequestParam("targetWord") String targetWord,
                                   @RequestParam("selectedWordId") int selectedWordId) {
        try {
            CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
            DeleteSourceWordRequest request = DeleteSourceWordRequest.builder()
                    .sourceWord(sourceWord)
                    .targetWord(targetWord)
                    .owner(principal.getUsername())
                    .selectedWordId(selectedWordId)
                    .build();
            ServiceResult<TranslationAttribute> result = translationService.deleteSourceWord(request);
            model.addAttribute("translationAttribute", result.value());
            model.addAttribute("translationLocalization", localizationService.translationFormLocalization(principal.sourceLanguage()));
            return "document-page/content/translation/update/update-translation-form";
        } catch (InvalidInputException e) {
            log.info("Invalid input, sending source-words-list template");
            model.addAttribute("translationAttribute", e.getResult().value());
            return "document-page/content/translation/update/update-translation-form";
        }
    }

    private ServiceResult<TranslationAttribute> createTranslation(AddTranslationRequest request) {
        return translationService.createTranslation(request);
    }
}
