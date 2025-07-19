package lule.dictionary.controller.importController;

import lule.dictionary.service.translation.dto.attribute.TranslationAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

public interface ImportController {
    @GetMapping("")
    String importListPage(Authentication authentication, Model model);

    @PostMapping({"/page/reload", "/page/reload/"})
    String importPageOnPost(@RequestAttribute("translationAttribute") TranslationAttribute translationAttribute,
                            @RequestParam("selectedWordId") int wordId,
                            @RequestParam("importId") int importId,
                            @RequestParam("page") int page,
                            Model model);

    @PutMapping({"/page/reload", "/page/reload/"})
    String importPageOnPut(@RequestAttribute("translationAttribute") TranslationAttribute translationAttribute,
                           @RequestParam("selectedWordId") int wordId,
                           @RequestParam("importId") int importId,
                           @RequestParam("page") int page,
                           Model model);
    @GetMapping({"/new", "/new/"})
    String createImportForm(Model model);

    @GetMapping({"/{importId}", "/{importId}/"})
    String importPage(@PathVariable("importId") int importId,
                                @RequestParam(name = "page", defaultValue = "1") int page,
                                Model model);

    @PostMapping({"/new", "/new/"})
    String createImport(@RequestParam("title") String title,
                               @RequestParam("pageContent") String content,
                               @RequestParam("url") String url,
                               Model model,
                               Authentication authentication);

    @GetMapping({"/url-form", "/url-form/"})
    String urlForm();

    @GetMapping({"/pageContent-form", "/pageContent-form/"})
    String contentForm();
}
