package lule.dictionary.translations.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lule.dictionary.translations.data.attribute.BaseFlashcardAttribute;
import lule.dictionary.translations.data.attribute.FlashcardConfigAttribute;
import lule.dictionary.translations.data.attribute.WordCardAttribute;
import lule.dictionary.translations.data.request.*;
import lule.dictionary.translations.service.TranslationService;
import lule.dictionary.translations.service.exception.TranslationsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping({"/vocabulary", "/vocabulary/"})
public class VocabularyController {

    private final TranslationService translationService;

    @GetMapping({"", "/"})
    public String vocabularyPage(Model model,
                                 Authentication authentication) {
        FlashcardConfigAttribute attribute = translationService.getFlashcardConfig(ConfigureFlashcardRequest.of(0, 10, false), authentication);
        model.addAttribute("attribute", attribute);
        return "vocabulary-page/base-page";
    }

    @GetMapping({"/config-form", "/config-form/"})
    public String configForm(@RequestParam(name = "familiarity", required = false, defaultValue = "0") int familiarity,
                             @RequestParam(name = "quantity", required = false, defaultValue = "10") int quantity,
                             @RequestParam(name = "isPhrase", required = false, defaultValue = "false") boolean isPhrase,
                             Model model,
                             Authentication authentication) {
        FlashcardConfigAttribute attribute = translationService.getFlashcardConfig(ConfigureFlashcardRequest.of(familiarity, quantity, isPhrase), authentication);
        model.addAttribute("attribute", attribute);
        return "vocabulary-page/flashcard/flashcard-config";
    }

    @GetMapping({"/flashcard/", "/flashcard/"})
    public String initializeFlashcardSet(@RequestParam(name = "familiarity", required = false, defaultValue = "0") int familiarity,
                                         @RequestParam(name = "quantity", required = false, defaultValue = "10") int quantity,
                                         @RequestParam(name = "isPhrase", required = false, defaultValue = "false") boolean isPhrase,
                                         @RequestParam(name = "id", required = false, defaultValue = "1") int id,
                                         Model model,
                                         Authentication authentication,
                                         HttpSession session) {
        try {
            BaseFlashcardAttribute attribute = translationService.getRandomTranslations(GetRandomTranslationsRequest.builder()
                    .familiarity(familiarity)
                    .quantity(quantity)
                    .isPhrase(isPhrase)
                    .id(id)
                    .build(), authentication);
            model.addAttribute("attribute", attribute);
            session.removeAttribute("translations");
            session.setAttribute("translations", attribute.translations());
            return "vocabulary-page/flashcard/flashcard";
        } catch (TranslationsNotFoundException e) {
            model.addAttribute("attribute", e.getAttribute());
            return "vocabulary-page/flashcard/flashcard-config";
        }
    }

    @GetMapping({"/flashcard/flip", "/flashcard/flip/"})
    public String flipFlashcard(@RequestParam(name = "familiarity", required = false, defaultValue = "0") int familiarity,
                                @RequestParam(name = "quantity", required = false, defaultValue = "10") int quantity,
                                @RequestParam(name = "isPhrase", required = false, defaultValue = "false") boolean isPhrase,
                                @RequestParam(name = "id", required = false, defaultValue = "1") int id,
                                Model model,
                                HttpSession session,
                                Authentication authentication) {
        BaseFlashcardAttribute attribute = translationService.flipFlashcard(FlipFlashcardRequest.builder()
                .id(id)
                .familiarity(familiarity)
                .quantity(quantity)
                .isPhrase(isPhrase)
                .build(), authentication, session);
        model.addAttribute("attribute", attribute);
        return "vocabulary-page/flashcard/flashcard";
    }

    @GetMapping({"/flashcard/flip-to-source-word", "/flashcard/flip-to-source-word/"})
    public String flipToSourceWord(@RequestParam("sourceWord") List<String> sourceWord,
                                   @RequestParam("targetWord") String targetWord,
                                   Model model) {
        WordCardAttribute attribute = translationService.getCardAttribute(GetCardAttributeRequest.of(sourceWord, targetWord));
        model.addAttribute("attribute", attribute);
        return "vocabulary-page/flashcard/source-word-card";
    }

    @GetMapping({"/flashcard/flip-to-target-word", "/flashcard/flip-to-target-word/"})
    public String flipToTargetWord(@RequestParam("sourceWord") List<String> sourceWord,
                                   @RequestParam("targetWord") String targetWord,
                                   Model model) {
        WordCardAttribute attribute = translationService.getCardAttribute(GetCardAttributeRequest.of(sourceWord, targetWord));
        model.addAttribute("attribute", attribute);
        return "vocabulary-page/flashcard/target-word-card";
    }
}
