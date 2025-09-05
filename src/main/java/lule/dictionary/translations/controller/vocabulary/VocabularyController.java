package lule.dictionary.translations.controller.vocabulary;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lule.dictionary.translations.controller.vocabulary.dto.BaseFlashcardAttribute;
import lule.dictionary.translations.controller.vocabulary.dto.FlashcardConfigLocalizationAttribute;
import lule.dictionary.translations.controller.vocabulary.dto.GetRandomTranslationsRequest;
import lule.dictionary.translations.data.Translation;
import lule.dictionary.userProfiles.data.CustomUserDetails;
import lule.dictionary.language.service.Language;
import lule.dictionary.localization.service.LocalizationService;
import lule.dictionary.session.service.SessionHelper;
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
    private final SessionHelper sessionHelper;
    private final LocalizationService localizationService;

    @GetMapping({"", "/"})
    public String vocabularyPage(Model model,
                                 Authentication authentication) {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute("flashcardConfigLocalizationAttribute", FlashcardConfigLocalizationAttribute.builder()
                .familiarityText(localizationService.flashcardConfigLocalization(principal.userInterfaceLanguage()).get("familiarity"))
                .howManyText(localizationService.flashcardConfigLocalization(principal.userInterfaceLanguage()).get("how_many"))
                .phrasesText(localizationService.flashcardConfigLocalization(principal.userInterfaceLanguage()).get("translationMapper"))
                .wordsText(localizationService.flashcardConfigLocalization(principal.userInterfaceLanguage()).get("words"))
                .reviewTranslationsText(localizationService.flashcardConfigLocalization(principal.userInterfaceLanguage()).get("review_translations"))
                .build());
        return "vocabulary-page/base-page";
    }

    @GetMapping({"/config-form", "/config-form/"})
    public String configForm(@RequestParam(name = "familiarity", required = false, defaultValue = "0") int familiarity,
                             @RequestParam(name = "quantity", required = false, defaultValue = "10") int quantity,
                             @RequestParam(name = "isPhrase", required = false, defaultValue = "false") boolean isPhrase,
                             Model model,
                             Authentication authentication) {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute("familiarity", familiarity);
        model.addAttribute("quantity", quantity);
        model.addAttribute("isPhrase", isPhrase);
        model.addAttribute("flashcardConfigLocalizationAttribute", FlashcardConfigLocalizationAttribute.builder()
                .familiarityText(localizationService.flashcardConfigLocalization(principal.userInterfaceLanguage()).get("familiarity"))
                .howManyText(localizationService.flashcardConfigLocalization(principal.userInterfaceLanguage()).get("how_many"))
                .phrasesText(localizationService.flashcardConfigLocalization(principal.userInterfaceLanguage()).get("translationMapper"))
                .wordsText(localizationService.flashcardConfigLocalization(principal.userInterfaceLanguage()).get("words"))
                .reviewTranslationsText(localizationService.flashcardConfigLocalization(principal.userInterfaceLanguage()).get("review_translations"))
                .build());
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
        String username = ((CustomUserDetails) authentication.getPrincipal()).getUsername();
        Language uiLanguage = ((CustomUserDetails) authentication.getPrincipal()).userInterfaceLanguage();
        try {
            BaseFlashcardAttribute attribute = translationService.getRandomTranslations(GetRandomTranslationsRequest.builder()
                    .familiarity(familiarity)
                    .quantity(quantity)
                    .isPhrase(isPhrase)
                    .id(id)
                    .owner(username)
                    .build());
            session.removeAttribute("translations");
            session.setAttribute("translations", attribute.translations());
            model.addAttribute("attribute", attribute);
            return "vocabulary-page/flashcard/flashcard";

        } catch (TranslationsNotFoundException e) {
            model.addAttribute("familiarity", familiarity);
            model.addAttribute("quantity", quantity);
            model.addAttribute("isPhrase", isPhrase);
            model.addAttribute("flashcardConfigLocalizationAttribute", FlashcardConfigLocalizationAttribute.builder()
                    .familiarityText(localizationService.flashcardConfigLocalization(uiLanguage).get("familiarity"))
                    .howManyText(localizationService.flashcardConfigLocalization(uiLanguage).get("how_many"))
                    .phrasesText(localizationService.flashcardConfigLocalization(uiLanguage).get("translationMapper"))
                    .wordsText(localizationService.flashcardConfigLocalization(uiLanguage).get("words"))
                    .reviewTranslationsText(localizationService.flashcardConfigLocalization(uiLanguage).get("review_translations"))
                    .build());
            return "vocabulary-page/flashcard/flashcard-config";
        }
    }

    @GetMapping({"/flashcard/flip", "/flashcard/flip/"})
    public String flipFlashcard(@RequestParam(name = "familiarity", required = false, defaultValue = "0") int familiarity,
                                @RequestParam(name = "quantity", required = false, defaultValue = "10") int quantity,
                                @RequestParam(name = "isPhrase", required = false, defaultValue = "false") boolean isPhrase,
                                @RequestParam(name = "id", required = false, defaultValue = "1") int id,
                                Model model,
                                HttpSession session) {
        List<Translation> translations = sessionHelper.getList(session, "translations", Translation.class);
        BaseFlashcardAttribute attribute = BaseFlashcardAttribute.builder()
                .id(id)
                .size(translations.size())
                .familiarity(familiarity)
                .quantity(quantity)
                .isPhrase(isPhrase)
                .translations(translations)
                .build();
        model.addAttribute("attribute", attribute);
        return "vocabulary-page/flashcard/flashcard";
    }

    @GetMapping({"/flashcard/flip-to-source-word", "/flashcard/flip-to-source-word/"})
    public String flashcardFlipToSourceWord(@RequestParam("sourceWord") List<String> sourceWord,
                                            @RequestParam("targetWord") String targetWord,
                                            Model model) {
        model.addAttribute("sourceWord", sourceWord);
        model.addAttribute("targetWord", targetWord);
        return "vocabulary-page/flashcard/source-word-screen";
    }

    @GetMapping({"/flashcard/flip-to-target-word", "/flashcard/flip-to-target-word/"})
    public String flashcardFlipToTargetWord(@RequestParam("sourceWord") List<String> sourceWord,
                                            @RequestParam("targetWord") String targetWord,
                                            Model model) {
        model.addAttribute("sourceWord", sourceWord);
        model.addAttribute("targetWord", targetWord);
        return "vocabulary-page/flashcard/target-word-screen";
    }
}
