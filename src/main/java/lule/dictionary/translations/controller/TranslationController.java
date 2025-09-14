package lule.dictionary.translations.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import lule.dictionary.translations.data.request.*;
import lule.dictionary.translations.data.Familiarity;
import lule.dictionary.language.service.Language;
import lule.dictionary.translations.service.TranslationService;
import lule.dictionary.translations.data.attribute.TranslationAttribute;
import lule.dictionary.translations.service.exception.TranslationServiceException;
import lule.dictionary.userProfiles.data.UserProfile;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Controller
@RequiredArgsConstructor
@RequestMapping("/translations")
@Slf4j
public class TranslationController {

    private final TranslationService translationService;

    @GetMapping({"/find", "/find/"})
    public String findOrAddTranslation(Model model,
                                       Authentication authentication,
                                       @RequestParam int documentId,
                                       @RequestParam String targetWord,
                                       @RequestParam("selectedWordId") int selectedWordId,
                                       @RequestParam(value = "isPhrase", required = false, defaultValue = "false") boolean isPhrase,
                                       @RequestParam("isFound") boolean isFound) {
        if(!isFound) {
            TranslationAttribute translationAttribute = translationService.translate(CreateTranslationRequest.builder()
                    .documentId(documentId)
                    .selectedWordId(selectedWordId)
                    .isPhrase(isPhrase)
                    .targetWord(targetWord)
                    .build(), authentication);
            model.addAttribute("attribute", translationAttribute);
            return "document-page/content/translation/add/add-translation-form";
        }
        UserProfile principal = (UserProfile) authentication.getPrincipal();
        FindByTargetWordRequest request = FindByTargetWordRequest.builder()
                .documentId(documentId)
                .selectedWordId(selectedWordId)
                .targetWord(targetWord)
                .sourceLanguage(principal.sourceLanguage())
                .targetLanguage(principal.targetLanguage())
                .owner(principal.getUsername())
                .isPhrase(isPhrase)
                .systemLanguage(principal.userInterfaceLanguage())
                .build();
        TranslationAttribute attribute = translationService.findByTargetWord(request);
        model.addAttribute("attribute", attribute);
        return "document-page/content/translation/update/update-translation-form";
    }

    @GetMapping({"/create-phrase", "/create-phrase/"})
    public String createPhrase(Model model,
                               Authentication authentication,
                               @RequestParam("selectableId") int selectableId,
                               @RequestParam("phraseText") String phraseText,
                               @RequestParam("documentId") int documentId,
                               @RequestParam("phraseLength") int phraseLength,
                               @RequestParam("familiarities") List<String> familiarities,
                               @RequestParam("isSavedList") List<String> isSavedList) {
        TranslationAttribute translationAttribute = translationService.translate(CreateTranslationRequest.builder()
                .targetWord(phraseText)
                .documentId(documentId)
                .selectedWordId(selectableId)
                .isPhrase(true)
                .build(), authentication);
        model.addAttribute("selectableId", selectableId);
        model.addAttribute("phraseText", phraseText);
        model.addAttribute("documentId", documentId);
        model.addAttribute("phraseLength", phraseLength);
        model.addAttribute("familiarities", familiarities);
        model.addAttribute("isSavedList", isSavedList);
        model.addAttribute("attribute", translationAttribute);
        return "document-page/content/new-phrase";
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
                                 @RequestParam("selectedWordId") int selectedWordId,
                                 @RequestParam(value = "isPhrase", required = false) boolean isPhrase) {
        try {
            AddTranslationRequest request = AddTranslationRequest.builder()
                    .documentId(documentId)
                    .selectedWordId(selectedWordId)
                    .sourceWords(sourceWords)
                    .targetWord(targetWord)
                    .sourceLanguage(sourceLanguage)
                    .targetLanguage(targetLanguage)
                    .familiarity(familiarity)
                    .isPhrase(isPhrase)
                    .build();
            TranslationAttribute attribute = translationService.createTranslation(request, authentication);
            model.addAttribute("attribute", attribute);
            return "document-page/content/translation/update/update-translation-form";
        } catch (TranslationServiceException e) {
            log.info(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping({"/familiarity/update", "/familiarity/update/"})
    public String updateFamiliarity(Model model,
                                    Authentication authentication,
                                    @RequestParam("targetWord") String targetWord,
                                    @RequestParam("familiarity") Familiarity familiarity,
                                    @RequestParam("sourceLanguage") Language sourceLanguage,
                                    @RequestParam("targetLanguage") Language targetLanguage,
                                    @RequestParam("selectedWordId") int selectedWordId,
                                    @RequestParam(value = "isPhrase", required = false) boolean isPhrase) {
        UserProfile principal = (UserProfile) authentication.getPrincipal();
        UpdateTranslationFamiliarityRequest request = UpdateTranslationFamiliarityRequest.builder()
                .selectedWordId(selectedWordId)
                .targetWord(targetWord)
                .sourceLanguage(sourceLanguage)
                .targetLanguage(targetLanguage)
                .familiarity(familiarity)
                .owner(principal.getUsername())
                .isPhrase(isPhrase)
                .systemLanguage(principal.userInterfaceLanguage())
                .build();
        TranslationAttribute result = translationService.updateFamiliarity(request);
        model.addAttribute("attribute", result);
        return "document-page/content/translation/update/update-translation-form";
    }

    @PutMapping({"/sourceWords/update", "/sourceWords/update/"})
    public String updateSourceWords(Model model,
                                    Authentication authentication,
                                    HttpSession session,
                                    @RequestParam("sourceWords") List<String> sourceWords,
                                    @RequestParam("targetWord") String targetWord,
                                    @RequestParam("familiarity") String currentFamiliarity,
                                    @RequestParam("selectedWordId") int selectedWordId,
                                    @RequestParam(value = "isPhrase", required = false) boolean isPhrase) {
        UserProfile principal = (UserProfile) authentication.getPrincipal();
        try {
            UpdateSourceWordsRequest request = UpdateSourceWordsRequest.builder()
                    .sourceWords(sourceWords)
                    .familiarity(Familiarity.valueOf(currentFamiliarity.toUpperCase()))
                    .sourceLanguage(principal.sourceLanguage())
                    .targetLanguage(principal.targetLanguage())
                    .owner(principal.getUsername())
                    .targetWord(targetWord)
                    .selectedWordId(selectedWordId)
                    .isPhrase(isPhrase)
                    .uiLanguage(principal.userInterfaceLanguage())
                    .build();
            TranslationAttribute attribute = translationService.updateSourceWords(request, session);
            model.addAttribute("error", Map.of());
            model.addAttribute("attribute", attribute);
            return "document-page/content/translation/update/update-translation-form";
        } catch (TranslationServiceException e) {
            log.warn("TranslationServiceException: {}", e.getViolation());
            model.addAttribute("error", e.getViolation());
            model.addAttribute("attribute", e.getAttribute());
            return "document-page/content/translation/update/update-translation-form";
        }
    }

    @DeleteMapping({"/sourceWords/delete", "/sourceWords/delete/"})
    public String deleteSourceWord(Model model,
                                   Authentication authentication,
                                   HttpSession session,
                                   @RequestParam("sourceWord") String sourceWord,
                                   @RequestParam("targetWord") String targetWord,
                                   @RequestParam("selectedWordId") int selectedWordId,
                                   @RequestParam(value = "isPhrase", required = false) boolean isPhrase) {
        UserProfile principal = (UserProfile) authentication.getPrincipal();
        DeleteSourceWordRequest request = DeleteSourceWordRequest.builder()
                .sourceWord(sourceWord)
                .targetWord(targetWord)
                .owner(principal.getUsername())
                .selectedWordId(selectedWordId)
                .isPhrase(isPhrase)
                .systemLanguage(principal.userInterfaceLanguage())
                .build();
        TranslationAttribute result = translationService.deleteSourceWord(request, session);
        model.addAttribute("attribute", result);
        return "document-page/content/translation/update/update-translation-form";
    }
}
