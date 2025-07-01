package lule.dictionary.controller.ollama;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.ollama4j.exceptions.OllamaBaseException;
import lombok.RequiredArgsConstructor;
import lule.dictionary.entity.application.interfaces.userProfile.CustomUserDetails;
import lule.dictionary.service.ollama.OllamaService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping({"/ollama"})
public class OllamaController {

    private final OllamaService ollamaService;

    @GetMapping({"/translate", "/translate/"})
    public String translate(@RequestParam(name = "targetWord") String targetWord,
                            Model model,
                            Authentication authentication) throws OllamaBaseException, IOException, InterruptedException {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        String translationJson = ollamaService.translate(principal.targetLanguage(), principal.sourceLanguage(), targetWord);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<?, ?> translation = objectMapper.readValue(translationJson, Map.class);
        model.addAttribute("translation", translation.get("sourceWord"));
        model.addAttribute("targetWord", targetWord);
        return "import-page/translation/ai-translation";
    }

    @GetMapping({"/closeAiTranslation", "/closeAiTranslation/"})
    public String closeAiTranslation(@RequestParam(name = "targetWord") String targetWord,
                                     Model model) {
        model.addAttribute("targetWord", targetWord);
        return "import-page/translation/get-ai-translation-btn";
    }
}
