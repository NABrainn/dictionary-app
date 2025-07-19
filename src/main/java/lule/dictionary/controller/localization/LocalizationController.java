package lule.dictionary.controller.localization;

import jakarta.servlet.http.HttpSession;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public interface LocalizationController {
    @GetMapping("/systemLanguage")
    ResponseEntity<String> setupInterfaceLanguage(@NonNull @RequestHeader("systemLanguage") String systemLanguage,
                                                  HttpSession httpSession);
}
