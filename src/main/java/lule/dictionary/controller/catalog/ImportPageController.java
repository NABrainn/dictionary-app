package lule.dictionary.controller.catalog;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.controller.catalog.dto.SaveTranslationRequest;
import lule.dictionary.service.imports.ImportService;
import lule.dictionary.service.console.ImportPageService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping({"/catalog", "/catalog/"})
@RequiredArgsConstructor
public class ImportPageController {

    private final ImportService importService;
    private final ImportPageService importPageService;

    @GetMapping({"", "/"})
    public String getImportList(Authentication authentication, Model model) {
        importService.findByOwner(model, authentication.getName());
        return "catalog";
    }

    @GetMapping({"/page/reload", "page/reload"})
    public String reloadOnPut(Model model,
                              @RequestParam("selectedWordId") int wordId,
                              @RequestParam("importId") int importId) {
        importPageService.loadImportWithTranslations(model, new SaveTranslationRequest(wordId, importId));
        return "import-page/content";
    }
}
