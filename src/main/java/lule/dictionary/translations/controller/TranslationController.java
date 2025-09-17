package lule.dictionary.translations.controller;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import lule.dictionary.translations.data.TranslationLocalizationKey;
import lule.dictionary.translations.data.attribute.PhraseAttribute;
import lule.dictionary.translations.data.request.*;
import lule.dictionary.translations.data.Familiarity;
import lule.dictionary.language.service.Language;
import lule.dictionary.translations.service.TranslationService;
import lule.dictionary.translations.data.attribute.TranslationAttribute;
import lule.dictionary.translations.data.exception.TranslationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
                                       @RequestParam("isPersisted") boolean isPersisted) {
        GetTranslationFormRequest request = isPersisted ?
                FindTranslationFormRequest.builder()
                        .documentId(documentId)
                        .selectedWordId(selectedWordId)
                        .isPhrase(isPhrase)
                        .targetWord(targetWord)
                        .build() :
                CreateTranslationFormRequest.builder()
                        .documentId(documentId)
                        .selectedWordId(selectedWordId)
                        .isPhrase(isPhrase)
                        .targetWord(targetWord)
                        .build();
        TranslationAttribute attribute = translationService.findOrCreateTranslation(request, authentication);
        Map<TranslationLocalizationKey, String> messages = translationService.getTranslationFormMessages(authentication);
        Map<String, Object> attributes = Map.of(
                "attribute", attribute,
                "messages", messages,
                "errors", Map.of()
        );
        model.addAllAttributes(attributes);
        return switch (attribute.type()) {
            case CREATE -> "translation/add-translation-form";
            case FIND -> "translation/update-translation-form";
        };
    }

    @GetMapping({"/create-phrase", "/create-phrase/"})
    public String createPhrase(Model model,
                               Authentication authentication,
                               @RequestParam("selectableId") int selectableId,
                               @RequestParam("documentId") int documentId,
                               @RequestParam("ids") List<Integer> ids,
                               @RequestParam("targetWords") List<String> targetWords,
                               @RequestParam("familiarities") List<String> familiarities,
                               @RequestParam("isPersistedList") List<String> isPersistedList) {
        CreatePhraseAttributeRequest request = CreatePhraseAttributeRequest.builder()
                .ids(ids)
                .unprocessedTargetWords(targetWords)
                .familiarities(familiarities)
                .isPersistedList(isPersistedList)
                .id(selectableId)
                .documentId(documentId)
                .build();
        PhraseAttribute attribute = translationService.createPhraseAttribute(request, authentication);
        Map<TranslationLocalizationKey, String> messages = translationService.getTranslationFormMessages(authentication);
        Map<String, Object> attributes = Map.of(
                "attribute", attribute,
                "messages", messages,
                "errors", Map.of()
        );
        model.addAllAttributes(attributes);
        return "translation/new-phrase";
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
            Map<TranslationLocalizationKey, String> messages = translationService.getTranslationFormMessages(authentication);
            Map<String, Object> attributes = Map.of(
                    "attribute", attribute,
                    "messages", messages,
                    "errors", Map.of()
            );
            model.addAllAttributes(attributes);
            return "translation/update-translation-form";
        } catch (TranslationServiceException e) {
            Map<TranslationLocalizationKey, String> messages = translationService.getTranslationFormMessages(authentication);
            Map<String, Object> attributes = Map.of(
                    "attribute", e.getAttribute(),
                    "messages", messages,
                    "errors", Map.of()
            );
            model.addAllAttributes(attributes);
            return "translation/add-translation-form";
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
        UpdateTranslationFamiliarityRequest request = UpdateTranslationFamiliarityRequest.builder()
                .selectedWordId(selectedWordId)
                .targetWord(targetWord)
                .sourceLanguage(sourceLanguage)
                .targetLanguage(targetLanguage)
                .familiarity(familiarity)
                .isPhrase(isPhrase)
                .build();
        TranslationAttribute attribute = translationService.updateFamiliarity(request, authentication);
        Map<TranslationLocalizationKey, String> messages = translationService.getTranslationFormMessages(authentication);
        Map<String, Object> attributes = Map.of(
                "attribute", attribute,
                "messages", messages,
                "errors", Map.of()
        );
        model.addAllAttributes(attributes);
        return "translation/update-translation-form";
    }

    @PutMapping({"/sourceWords/update", "/sourceWords/update/"})
    public String updateSourceWords(Model model,
                                    Authentication authentication,
                                    @RequestParam("sourceWords") List<String> sourceWords,
                                    @RequestParam("targetWord") String targetWord,
                                    @RequestParam("familiarity") String currentFamiliarity,
                                    @RequestParam("selectedWordId") int selectedWordId,
                                    @RequestParam(value = "isPhrase", required = false) boolean isPhrase) {
        try {
            UpdateSourceWordsRequest request = UpdateSourceWordsRequest.builder()
                    .sourceWords(sourceWords)
                    .familiarity(Familiarity.valueOf(currentFamiliarity.toUpperCase()))
                    .targetWord(targetWord)
                    .selectedWordId(selectedWordId)
                    .isPhrase(isPhrase)
                    .build();
            TranslationAttribute attribute = translationService.updateSourceWords(request, authentication);
            Map<TranslationLocalizationKey, String> messages = translationService.getTranslationFormMessages(authentication);
            Map<String, Object> attributes = Map.of(
                    "attribute", attribute,
                    "messages", messages,
                    "errors", Map.of()
            );
            model.addAllAttributes(attributes);
            return "translation/update-translation-form";

        } catch (TranslationServiceException e) {
            log.warn("TranslationServiceException: {}", e.getMessages());
            Map<TranslationLocalizationKey, String> messages = translationService.getTranslationFormMessages(authentication);
            Map<String, Object> attributes = Map.of(
                    "attribute", e.getAttribute(),
                    "messages", messages,
                    "errors", e.getMessages()
            );
            model.addAllAttributes(attributes);
            return "translation/update-translation-form";
        }
    }

    @DeleteMapping({"/sourceWords/delete", "/sourceWords/delete/"})
    public String deleteSourceWord(Model model,
                                   Authentication authentication,
                                   @RequestParam("sourceWord") String sourceWord,
                                   @RequestParam("targetWord") String targetWord,
                                   @RequestParam("selectedWordId") int selectedWordId,
                                   @RequestParam(value = "isPhrase", required = false) boolean isPhrase) {
        DeleteSourceWordRequest request = DeleteSourceWordRequest.builder()
                .sourceWord(sourceWord)
                .targetWord(targetWord)
                .selectedWordId(selectedWordId)
                .isPhrase(isPhrase)
                .build();
        TranslationAttribute attribute = translationService.deleteSourceWord(request, authentication);
        Map<TranslationLocalizationKey, String> messages = translationService.getTranslationFormMessages(authentication);
        Map<String, Object> attributes = Map.of(
                "attribute", attribute,
                "messages", messages,
                "errors", Map.of()
        );
        model.addAllAttributes(attributes);
        return "translation/update-translation-form";
    }
}
