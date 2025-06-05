package lule.dictionary.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import lule.dictionary.dto.application.interfaces.translation.Translation;
import lule.dictionary.enumeration.Familiarity;
import lule.dictionary.enumeration.Language;
import lule.dictionary.exception.ResourceNotFoundException;
import lule.dictionary.factory.dto.TranslationFactory;
import lule.dictionary.factory.dto.UserProfileFactory;
import lule.dictionary.service.application.entity.TranslationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.TreeMap;

@Controller
@RequiredArgsConstructor
@RequestMapping({"/translations", "/translations/"})
@Slf4j
public class TranslationController {

    private final TranslationService translationService;

    @GetMapping({"/", ""})
    public String findByTargetWord(Model model,
                                   @RequestParam int importId,
                                   @RequestParam String targetWord,
                                   @RequestParam("selectedWordId") int selectedWordId) {
        String targetWordWithoutSpecials = targetWord.replaceAll("[^\\p{L}\\p{N}]", "").trim().toLowerCase();
        try {
            Translation translation = translationService.findByTargetWord(targetWordWithoutSpecials);
            model.addAttribute("importId", importId);
            model.addAttribute("currentFamiliarity", translationService.getFamiliarityAsInt(translation.translationDetails().familiarity()));
            model.addAttribute("translation", translation);
            model.addAttribute("familiarityLevels", new TreeMap<>(Map.of(
                    1, Familiarity.UNKNOWN,
                    2, Familiarity.RECOGNIZED,
                    3, Familiarity.FAMILIAR,
                    4, Familiarity.KNOWN,
                    5, Familiarity.IGNORED)
            ));
            model.addAttribute("selectedWordId", selectedWordId);
            return "translation/update-translation-form";
        } catch (ResourceNotFoundException e) {
            model.addAttribute("importId", importId);
            model.addAttribute("currentFamiliarity", 1);
            model.addAttribute("translation", TranslationFactory.createTranslation(
                    TranslationFactory.createTranslationDetails(
                            "translationFromApi",
                            targetWordWithoutSpecials,
                            Familiarity.UNKNOWN
                    ),
                    UserProfileFactory.createSettings(
                            Language.EN,
                            Language.NO
                    ),
                    "username"
            ));
            model.addAttribute("familiarityLevels", new TreeMap<>(Map.of(
                    1, Familiarity.UNKNOWN,
                    2, Familiarity.RECOGNIZED,
                    3, Familiarity.FAMILIAR,
                    4, Familiarity.KNOWN,
                    5, Familiarity.IGNORED)
            ));
            model.addAttribute("selectedWordId", selectedWordId);
            return "translation/add-translation-form";
        }
    }

    @PostMapping({"/new", "new"})
    public String newTranslation(Model model,
            @RequestParam("sourceWord") String sourceWord,
            @RequestParam("targetWord") String targetWord,
            @RequestParam("familiarity") Familiarity familiarity,
            @RequestParam("sourceLanguage") Language sourceLanguage,
            @RequestParam("targetLanguage") Language targetLanguage,
            @RequestParam("owner") String owner,
            @RequestParam("importId") int importId,
            @RequestParam("selectedWordId") int selectedWordId
    ) {
        String targetWordWithoutSpecials = targetWord.replaceAll("[^\\p{L}\\p{N}]", "").trim().toLowerCase();
        Translation trans = TranslationFactory.createTranslation(
                TranslationFactory.createTranslationDetails(
                        sourceWord,
                        targetWordWithoutSpecials,
                        familiarity
                ),
                UserProfileFactory.createSettings(
                        sourceLanguage,
                        targetLanguage
                ),
                owner
        );
        translationService.add(trans, importId);
        model.addAttribute("importId", importId);
        model.addAttribute("currentFamiliarity", translationService.getFamiliarityAsInt(familiarity));
        model.addAttribute("translation", trans);
        model.addAttribute("familiarityLevels", new TreeMap<>(Map.of(
                1, Familiarity.UNKNOWN,
                2, Familiarity.RECOGNIZED,
                3, Familiarity.FAMILIAR,
                4, Familiarity.KNOWN,
                5, Familiarity.IGNORED)
        ));
        model.addAttribute("selectedWordId", selectedWordId);
        return "forward:/imports/page/reload";
    }

    @PutMapping({"/familiarity/update", "familiarity/update"})
    public String updateFamiliarity(Model model, HttpServletRequest request,
                                    @RequestParam("sourceWord") String sourceWord,
                                    @RequestParam("targetWord") String targetWord,
                                    @RequestParam("familiarity") Familiarity familiarity,
                                    @RequestParam("sourceLanguage") Language sourceLanguage,
                                    @RequestParam("targetLanguage") Language targetLanguage,
                                    @RequestParam("owner") String owner,
                                    @RequestParam("importId") int importId,
                                    @RequestParam("selectedWordId") int selectedWordId) {
        String targetWordWithoutSpecials = targetWord.replaceAll("[^\\p{L}\\p{N}]", "").trim().toLowerCase();
        Translation trans = TranslationFactory.createTranslation(
                TranslationFactory.createTranslationDetails(
                        sourceWord,
                        targetWordWithoutSpecials,
                        familiarity
                ),
                UserProfileFactory.createSettings(
                        sourceLanguage,
                        targetLanguage
                ),
                owner
        );
        translationService.updateFamiliarity(trans.translationDetails().targetWord(), trans.translationDetails().familiarity());
        model.addAttribute("importId", importId);
        model.addAttribute("currentFamiliarity", translationService.getFamiliarityAsInt(familiarity));
        model.addAttribute("translation", trans);
        model.addAttribute("familiarityLevels", new TreeMap<>(Map.of(
                1, Familiarity.UNKNOWN,
                2, Familiarity.RECOGNIZED,
                3, Familiarity.FAMILIAR,
                4, Familiarity.KNOWN,
                5, Familiarity.IGNORED)
        ));
        model.addAttribute("selectedWordId", selectedWordId);
        return "forward:/imports/page/reload";
    }
}
