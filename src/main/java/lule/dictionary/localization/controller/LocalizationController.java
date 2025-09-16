package lule.dictionary.localization.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lule.dictionary.language.service.Language;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.stream.Stream;
@Controller
@RequestMapping({"/localization", "/localization/"})
@RequiredArgsConstructor
public class LocalizationController {

    //TODO wire up to a service to separate sanitization logic
    @GetMapping("/uiLanguage")
    public void setupInterfaceLanguage(@RequestHeader("uiLanguage") String systemLanguage, HttpSession httpSession) {
        Stream.of(systemLanguage)
                .map(String::toUpperCase)
                .map(header -> {
                    try {
                        return Language.valueOf(header);
                    } catch (IllegalArgumentException e) {
                        return Language.EN;
                    }
                })
                .findFirst()
                .ifPresent(language -> httpSession.setAttribute("navbarLocalization", language));
    }
}