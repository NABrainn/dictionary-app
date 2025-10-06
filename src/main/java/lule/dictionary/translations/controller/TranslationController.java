package lule.dictionary.translations.controller;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import lule.dictionary.result.data.Err;
import lule.dictionary.result.data.Ok;
import lule.dictionary.result.data.Result;
import lule.dictionary.translations.data.TranslationLocalizationKey;
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

    @GetMapping({"", "/"})
    public String addOrUpdateTranslationForm(Model model,
                                             Authentication authentication,
                                             @RequestParam("id") int id,
                                             @RequestParam("documentId") int documentId,
                                             @RequestParam("targetWord") String targetWord,
                                             @RequestParam(value = "isPhrase", defaultValue = "false") boolean isPhrase,
                                             @RequestParam("isPersisted") boolean isPersisted) {
        GetTranslationFormRequest request = switch (isPersisted) {
            case true -> FindTranslationFormRequest.builder()
                    .documentId(documentId)
                    .selectedWordId(id)
                    .isPhrase(isPhrase)
                    .unprocessedTargetWord(targetWord)
                    .build();
            case false -> CreateTranslationFormRequest.builder()
                    .documentId(documentId)
                    .selectedWordId(id)
                    .isPhrase(isPhrase)
                    .unprocessedTargetWord(targetWord)
                    .build();
        };
        Result<TranslationAttribute> result = translationService.findOrCreateTranslation(request, authentication);
        Map<TranslationLocalizationKey, String> messages = translationService.getTranslationFormMessages(authentication);
        if (result instanceof Ok<TranslationAttribute>(TranslationAttribute value)) {
            Map<String, Object> attributes = Map.of(
                    "attribute", value,
                    "messages", messages,
                    "errors", Map.of()
            );
            model.addAllAttributes(attributes);
            return switch (value.type()) {
                case CREATE -> "translation/init-add-translation-form";
                case FIND -> "translation/init-update-translation-form";
            };
        }
        return "error";
    }

    @PostMapping({"", "/"})
    public String createTranslation(Model model,
                                 Authentication authentication,
                                 @RequestParam("sourceWords") List<String> sourceWords,
                                 @RequestParam("targetWord") String targetWord,
                                 @RequestParam("familiarity") Familiarity familiarity,
                                 @RequestParam("sourceLanguage") Language sourceLanguage,
                                 @RequestParam("targetLanguage") Language targetLanguage,
                                 @RequestParam("documentId") int documentId,
                                 @RequestParam("id") int selectedWordId,
                                 @RequestParam(value = "isPhrase", required = false) boolean isPhrase) {
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
        Result<TranslationAttribute> result = translationService.createTranslation(request, authentication);
        Map<TranslationLocalizationKey, String> messages = translationService.getTranslationFormMessages(authentication);
        return switch (result) {
            case Ok<TranslationAttribute> ok -> {
                Map<String, Object> attributes = Map.of(
                        "attribute", ok.value(),
                        "messages", messages,
                        "errors", Map.of()
                );
                model.addAllAttributes(attributes);
                yield  "translation/ok-update-translation-form";
            }
            case Err<?> err -> {
                if(err.throwable() instanceof TranslationServiceException e) {
                    Map<String, Object> attributes = Map.of(
                            "attribute", e.getAttribute(),
                            "messages", messages,
                            "errors", e.getMessages()
                    );
                    model.addAllAttributes(attributes);
                    yield  "translation/err-add-translation-form";
                }
                yield  "error";
            }
        };
    }

    @PutMapping({"/familiarity/update", "/familiarity/update/"})
    public String updateFamiliarity(Model model,
                                    Authentication authentication,
                                    @RequestParam("targetWord") String targetWord,
                                    @RequestParam("familiarity") Familiarity familiarity,
                                    @RequestParam("sourceLanguage") Language sourceLanguage,
                                    @RequestParam("targetLanguage") Language targetLanguage,
                                    @RequestParam("id") int selectedWordId,
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
        return "translation/ok-update-translation-form";
    }

    @PutMapping({"/sourceWords/update", "/sourceWords/update/"})
    public String updateSourceWords(Model model,
                                    Authentication authentication,
                                    @RequestParam("sourceWords") List<String> sourceWords,
                                    @RequestParam("targetWord") String targetWord,
                                    @RequestParam("familiarity") String currentFamiliarity,
                                    @RequestParam("id") int selectedWordId,
                                    @RequestParam(value = "isPhrase", required = false) boolean isPhrase) {
            UpdateSourceWordsRequest request = UpdateSourceWordsRequest.builder()
                    .sourceWords(sourceWords)
                    .familiarity(Familiarity.valueOf(currentFamiliarity.toUpperCase()))
                    .targetWord(targetWord)
                    .selectedWordId(selectedWordId)
                    .isPhrase(isPhrase)
                    .build();
            Result<TranslationAttribute> result = translationService.updateSourceWords(request, authentication);
            Map<TranslationLocalizationKey, String> messages = translationService.getTranslationFormMessages(authentication);
            switch (result) {
                case Ok<TranslationAttribute> ok -> {
                    Map<String, Object> attributes = Map.of(
                            "attribute", ok.value(),
                            "messages", messages,
                            "errors", Map.of()
                    );
                    model.addAllAttributes(attributes);
                    return "translation/ok-update-translation-form";
                }
                case Err<?> err -> {
                    if(err.throwable() instanceof TranslationServiceException e) {
                        Map<String, Object> attributes = Map.of(
                                "attribute", e.getAttribute(),
                                "messages", messages,
                                "errors", e.getMessages()
                        );
                        model.addAllAttributes(attributes);
                        return "translation/err-update-translation-form";
                    }
                    return "error";
                }
            }
    }

    @DeleteMapping({"/sourceWords", "/sourceWords/"})
    public String deleteSourceWord(Model model,
                                   Authentication authentication,
                                   @RequestParam("sourceWord") String sourceWord,
                                   @RequestParam("targetWord") String targetWord,
                                   @RequestParam("id") int selectedWordId,
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
        return "translation/ok-update-translation-form";
    }
}
