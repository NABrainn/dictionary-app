package lule.dictionary.localization.controller;

import jakarta.servlet.http.HttpSession;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lule.dictionary.language.service.Language;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({"/localization", "/localization/"})
@RequiredArgsConstructor
public class LocalizationControllerImp implements LocalizationController {

    @Override
    @GetMapping("/systemLanguage")
    public ResponseEntity<String> setupInterfaceLanguage(@NonNull @RequestHeader("systemLanguage") String systemLanguage,
                                                         HttpSession httpSession) {
        Language sourceLanguage = parseHeader(systemLanguage);
        httpSession.setAttribute("navbarLocalization", sourceLanguage);
        return ResponseEntity.ok("System language info updated to: " + sourceLanguage);
    }

    private Language parseHeader(String sourceLanguageHeader) {
        try {
            String formattedHeader = sourceLanguageHeader.toUpperCase();
            return Language.valueOf(formattedHeader);
        } catch (IllegalArgumentException e) {
            return Language.EN;
        }
    }
}