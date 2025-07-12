package lule.dictionary.controller.translation;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import lule.dictionary.entity.application.interfaces.userProfile.CustomUserDetails;
import lule.dictionary.service.dto.exception.InvalidInputException;
import lule.dictionary.service.dto.result.ServiceResult;
import lule.dictionary.service.translation.dto.*;
import lule.dictionary.enumeration.Familiarity;
import lule.dictionary.service.language.Language;
import lule.dictionary.service.translation.TranslationServiceImp;
import lule.dictionary.service.translation.dto.attribute.TranslationAttribute;
import lule.dictionary.service.translation.dto.attribute.TranslationPair;
import lule.dictionary.service.translation.dto.request.AddTranslationRequest;
import lule.dictionary.service.translation.dto.request.FindByTargetWordRequest;
import lule.dictionary.service.translation.dto.request.UpdateSourceWordsRequest;
import lule.dictionary.service.translation.dto.request.UpdateTranslationFamiliarityRequest;
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
        try {
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
        } catch (InvalidInputException e) {
            String exceptionMessage = "Failed to add translation due to invalid input.";
            log.info(exceptionMessage);
            throw new RuntimeException(exceptionMessage);
        }
    }

    @GetMapping("")
    public String findByTargetWord(Model model,
                                   Authentication authentication,
                                   @RequestParam int importId,
                                   @RequestParam String targetWord,
                                   @RequestParam("selectedWordId") int selectedWordId,
                                   @RequestParam("page") int page) {
        try {
            CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
            ServiceResult<TranslationAttribute> result = translationService.findByTargetWord(FindByTargetWordRequest.builder()
                    .importId(importId)
                    .selectedWordId(selectedWordId)
                    .targetWord(targetWord)
                    .sourceLanguage(principal.sourceLanguage())
                    .targetLanguage(principal.targetLanguage())
                    .page(page)
                    .owner(principal.getUsername())
                    .build());
            model.addAttribute("translationAttribute", result.value());
            return "import-page/translation/update-translation-form";

        } catch (TranslationNotFoundException e) {
            log.info("Translation not found, sending add-translation-form template: {}", e.getResult().value());
            model.addAttribute("translationAttribute", e.getResult().value());
            return "import-page/translation/add-translation-form";

        } catch (InvalidInputException e) {
            log.info("Invalid input, resending update-translation-form template");
            model.addAttribute("translationAttribute", e.getResult().value());
            return "import-page/translation/update-translation-form";
        }
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
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        ServiceResult<TranslationAttribute> result = translationService.updateFamiliarity(UpdateTranslationFamiliarityRequest.builder()
                .importId(importId)
                .selectedWordId(selectedWordId)
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

    @PutMapping({"/sourceWords/update", "/sourceWords/update/"})
    public String updateSourceWords(Model model,
                                    Authentication authentication,
                                    @RequestParam("sourceWords") List<String> sourceWords,
                                    @RequestParam("targetWord") String targetWord) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        try {
            ServiceResult<TranslationPair> result = translationService.updateSourceWords(new UpdateSourceWordsRequest(
                    sourceWords,
                    targetWord,
                    userDetails.getUsername()
            ));
            model.addAttribute("translationPair", result.value());
            model.addAttribute("hasError", result.hasError());
            return "import-page/translation/update-source-words-form";
        } catch (InvalidInputException e) {
            model.addAttribute("translationPair", e.getResult().value());
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
            ServiceResult<TranslationPair> result = translationService.deleteSourceWord(new DeleteSourceWordRequest(
                    sourceWord,
                    targetWord,
                    userDetails.getUsername()
            ));
            model.addAttribute("translationPair", TranslationPair.of(result.value().sourceWords(), result.value().targetWord()));
            return "import-page/translation/source-words-list";
        } catch (InvalidInputException e) {
            log.info("Invalid input, sending source-words-list template");
            model.addAttribute("translationAttribute", e.getResult().value());
            return "import-page/translation/source-words-list";
        }
    }
}
