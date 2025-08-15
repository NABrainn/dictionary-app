package lule.dictionary.controller.vocabularyController;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lule.dictionary.controller.vocabularyController.dto.BaseFlashcardAttribute;
import lule.dictionary.controller.vocabularyController.dto.FlashcardConfigLocalizationAttribute;
import lule.dictionary.controller.vocabularyController.dto.GetRandomTranslationsRequest;
import lule.dictionary.dto.application.result.ServiceResult;
import lule.dictionary.dto.database.interfaces.translation.Translation;
import lule.dictionary.dto.database.interfaces.userProfile.CustomUserDetails;
import lule.dictionary.service.localization.LocalizationService;
import lule.dictionary.service.sessionHelper.SessionHelper;
import lule.dictionary.service.translation.TranslationService;
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
                .familiarityText(localizationService.flashcardConfigLocalization(principal.sourceLanguage()).get("familiarity"))
                .howManyText(localizationService.flashcardConfigLocalization(principal.sourceLanguage()).get("how_many"))
                .phrasesText(localizationService.flashcardConfigLocalization(principal.sourceLanguage()).get("phrases"))
                .wordsText(localizationService.flashcardConfigLocalization(principal.sourceLanguage()).get("words"))
                .reviewTranslationsText(localizationService.flashcardConfigLocalization(principal.sourceLanguage()).get("review_translations"))
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
                .familiarityText(localizationService.flashcardConfigLocalization(principal.sourceLanguage()).get("familiarity"))
                .howManyText(localizationService.flashcardConfigLocalization(principal.sourceLanguage()).get("how_many"))
                .phrasesText(localizationService.flashcardConfigLocalization(principal.sourceLanguage()).get("phrases"))
                .wordsText(localizationService.flashcardConfigLocalization(principal.sourceLanguage()).get("words"))
                .reviewTranslationsText(localizationService.flashcardConfigLocalization(principal.sourceLanguage()).get("review_translations"))
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
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        ServiceResult<BaseFlashcardAttribute> attribute = translationService.getRandomTranslations(GetRandomTranslationsRequest.builder()
                .familiarity(familiarity)
                .quantity(quantity)
                .isPhrase(isPhrase)
                .id(id)
                .owner(principal.getUsername())
                .build());
        session.removeAttribute("translations");
        if(!attribute.hasError()) {
            session.setAttribute("translations", attribute.value().translations());
            model.addAttribute("attribute", attribute.value());
            return "vocabulary-page/flashcard/flashcard";
        }
        model.addAttribute("familiarity", familiarity);
        model.addAttribute("quantity", quantity);
        model.addAttribute("isPhrase", isPhrase);
        model.addAttribute("messages", attribute.messages());
        model.addAttribute("flashcardConfigLocalizationAttribute", FlashcardConfigLocalizationAttribute.builder()
                .familiarityText(localizationService.flashcardConfigLocalization(principal.sourceLanguage()).get("familiarity"))
                .howManyText(localizationService.flashcardConfigLocalization(principal.sourceLanguage()).get("how_many"))
                .phrasesText(localizationService.flashcardConfigLocalization(principal.sourceLanguage()).get("phrases"))
                .wordsText(localizationService.flashcardConfigLocalization(principal.sourceLanguage()).get("words"))
                .reviewTranslationsText(localizationService.flashcardConfigLocalization(principal.sourceLanguage()).get("review_translations"))
                .build());
        return "vocabulary-page/flashcard/flashcard-config";
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
