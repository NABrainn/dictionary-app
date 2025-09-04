package lule.dictionary.translations.controller;

import lule.dictionary.enumeration.Familiarity;
import lule.dictionary.language.service.Language;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface TranslationController {
    String newTranslation(Model model,
                          Authentication authentication,
                          @RequestParam("sourceWords") List<String> sourceWords,
                          @RequestParam("targetWord") String targetWord,
                          @RequestParam("familiarity") Familiarity familiarity,
                          @RequestParam("sourceLanguage") Language sourceLanguage,
                          @RequestParam("targetLanguage") Language targetLanguage,
                          @RequestParam("documentId") int importId,
                          @RequestParam("selectedWordId") int selectedWordId,
                          @RequestParam("page") int page,
                          @RequestParam("left") String left,
                          @RequestParam("right") String right,
                          @RequestParam("top") String top,
                          @RequestParam("bottom") String bottom);

}
