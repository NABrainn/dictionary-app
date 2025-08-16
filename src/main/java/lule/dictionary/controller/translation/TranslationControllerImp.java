package lule.dictionary.controller.translation;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import lule.dictionary.dto.database.implementation.translation.base.TranslationImp;
import lule.dictionary.dto.database.interfaces.userProfile.CustomUserDetails;
import lule.dictionary.exception.application.InvalidInputException;
import lule.dictionary.dto.application.result.ServiceResult;
import lule.dictionary.enumeration.Familiarity;
import lule.dictionary.service.language.Language;
import lule.dictionary.service.localization.LocalizationService;
import lule.dictionary.service.translation.TranslationServiceImp;
import lule.dictionary.service.translation.dto.attribute.TranslationAttribute;
import lule.dictionary.service.translation.dto.request.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Controller
@RequiredArgsConstructor
@RequestMapping("/translations")
@Slf4j
public class TranslationControllerImp {

    private final TranslationServiceImp translationService;
    private final LocalizationService localizationService;

    @GetMapping({"/find", "/find/"})
    public String findOrAddTranslation(Model model,
                                       Authentication authentication,
                                       @RequestParam int documentId,
                                       @RequestParam String targetWord,
                                       @RequestParam("selectedWordId") int selectedWordId,
                                       @RequestParam(value = "isPhrase", required = false) boolean isPhrase,
                                       @RequestParam("isFound") boolean isFound) {
        if(!isFound) {
            CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
            List<String> sourceWords = translationService.translate(TranslateRequest.of(targetWord, principal.sourceLanguage(), principal.targetLanguage()));
            model.addAttribute("translationAttribute", TranslationAttribute.builder()
                    .documentId(documentId)
                    .selectedWordId(selectedWordId)
                    .translationId(-1)
                    .translation(TranslationImp.builder()
                            .sourceWords(sourceWords)
                            .targetWord(targetWord)
                            .familiarity(Familiarity.UNKNOWN)
                            .sourceLanguage(principal.sourceLanguage())
                            .targetLanguage(principal.targetLanguage())
                            .owner(principal.getUsername())
                            .isPhrase(isPhrase)
                            .build())
                    .currentFamiliarity(getFamiliarityAsDigit(Familiarity.UNKNOWN))
                    .isPhrase(isPhrase)
                    .familiarityLevels(getFamiliarityTable())
                    .build());
            model.addAttribute("translationLocalization", localizationService.translationFormLocalization(principal.userInterfaceLanguage()));
            return "document-page/content/translation/add/add-translation-form";
        }
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        FindByTargetWordRequest request = FindByTargetWordRequest.builder()
                .documentId(documentId)
                .selectedWordId(selectedWordId)
                .targetWord(targetWord)
                .sourceLanguage(principal.sourceLanguage())
                .targetLanguage(principal.targetLanguage())
                .owner(principal.getUsername())
                .isPhrase(isPhrase)
                .build();
        ServiceResult<TranslationAttribute> result = translationService.findByTargetWord(request);
        model.addAttribute("translationAttribute", result.value());
        model.addAttribute("translationLocalization", localizationService.translationFormLocalization(principal.userInterfaceLanguage()));
        return "document-page/content/translation/update/update-translation-form";
    }

    @GetMapping({"/unselect-phrase", "/unselect-phrase/"})
    public String unselectPhrase(@RequestParam("targetWords") String targetWords,
                                 @RequestParam("selectableId") int selectableId,
                                 @RequestParam("documentId") int documentId,
                                 @RequestParam("familiarities") List<String> familiarities,
                                 Model model) {
        model.addAttribute("targetWords", targetWords);
        model.addAttribute("selectableId", selectableId);
        model.addAttribute("documentId", documentId);
        model.addAttribute("familiarities", familiarities);
        return "document-page/content/unselected-phrase";
    }

    @GetMapping({"/phrase", "/phrase/"})
    public String newPhrase(@RequestParam("targetWords") String targetWords,
                            @RequestParam("selectableId") int selectableId,
                            @RequestParam("documentId") int documentId,
                            @RequestParam("familiarities") List<String> familiarities,
                            Model model) {
        model.addAttribute("targetWords", targetWords);
        model.addAttribute("selectableId", selectableId);
        model.addAttribute("documentId", documentId);
        model.addAttribute("familiarities", familiarities);
        return "document-page/content/selected-phrase";
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
                    .isPhrase(isPhrase)
                    .build();
            ServiceResult<TranslationAttribute> result = createTranslation(request);
            model.addAttribute("translationAttribute", result.value());
            model.addAttribute("translationLocalization", localizationService.translationFormLocalization(principal.userInterfaceLanguage()));
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
                                    @RequestParam("selectedWordId") int selectedWordId,
                                    @RequestParam(value = "isPhrase", required = false) boolean isPhrase) {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        UpdateTranslationFamiliarityRequest request = UpdateTranslationFamiliarityRequest.builder()
                .selectedWordId(selectedWordId)
                .targetWord(targetWord)
                .sourceLanguage(sourceLanguage)
                .targetLanguage(targetLanguage)
                .familiarity(familiarity)
                .owner(principal.getUsername())
                .isPhrase(isPhrase)
                .build();
        ServiceResult<TranslationAttribute> result = translationService.updateFamiliarity(request);
        model.addAttribute("translationAttribute", result.value());
        model.addAttribute("translationLocalization", localizationService.translationFormLocalization(principal.userInterfaceLanguage()));
        return "document-page/content/translation/update/update-translation-form";
    }

    @PutMapping({"/sourceWords/update", "/sourceWords/update/"})
    public String updateSourceWords(Model model,
                                    Authentication authentication,
                                    @RequestParam("sourceWords") List<String> sourceWords,
                                    @RequestParam("targetWord") String targetWord,
                                    @RequestParam("familiarity") String currentFamiliarity,
                                    @RequestParam("selectedWordId") int selectedWordId,
                                    @RequestParam(value = "isPhrase", required = false) boolean isPhrase) {
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
                    .isPhrase(isPhrase)
                    .build();
            ServiceResult<TranslationAttribute> result = translationService.updateSourceWords(request);
            model.addAttribute("translationAttribute", result.value());
            model.addAttribute("translationLocalization", localizationService.translationFormLocalization(principal.userInterfaceLanguage()));
            model.addAttribute("hasError", result.hasError());
            return "document-page/content/translation/update/update-translation-form";
        } catch (InvalidInputException e) {
            log.warn("Invalid input: returning back");
            model.addAttribute("translationAttribute", e.getResult().value());
            model.addAttribute("translationLocalization", localizationService.translationFormLocalization(principal.userInterfaceLanguage()));
            return "document-page/content/translation/update/update-translation-form";
        }
    }

    @DeleteMapping({"/sourceWords/delete", "/sourceWords/delete/"})
    public String deleteSourceWord(Model model,
                                   Authentication authentication,
                                   @RequestParam("sourceWord") String sourceWord,
                                   @RequestParam("targetWord") String targetWord,
                                   @RequestParam("selectedWordId") int selectedWordId,
                                   @RequestParam(value = "isPhrase", required = false) boolean isPhrase) {
        try {
            CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
            DeleteSourceWordRequest request = DeleteSourceWordRequest.builder()
                    .sourceWord(sourceWord)
                    .targetWord(targetWord)
                    .owner(principal.getUsername())
                    .selectedWordId(selectedWordId)
                    .isPhrase(isPhrase)
                    .build();
            ServiceResult<TranslationAttribute> result = translationService.deleteSourceWord(request);
            model.addAttribute("translationAttribute", result.value());
            model.addAttribute("translationLocalization", localizationService.translationFormLocalization(principal.userInterfaceLanguage()));
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

    private int getFamiliarityAsDigit(Familiarity familiarity) {
        return switch (familiarity) {
            case UNKNOWN -> 1;
            case RECOGNIZED -> 2;
            case FAMILIAR -> 3;
            case KNOWN -> 4;
            case IGNORED -> 5;
        };
    }

    private Map<Integer, Familiarity> getFamiliarityTable() {
        return new TreeMap<>(Map.of(
                1, Familiarity.UNKNOWN,
                2, Familiarity.RECOGNIZED,
                3, Familiarity.FAMILIAR,
                4, Familiarity.KNOWN,
                5, Familiarity.IGNORED)
        );
    }
}
