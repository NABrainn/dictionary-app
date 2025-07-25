package lule.dictionary.controller.importController;

import lule.dictionary.service.translation.dto.attribute.TranslationAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

public interface ImportController {
    @GetMapping("")
    String importListPage(Model model,
                          Authentication authentication);

    @GetMapping({"/new", "/new/"})
    String createImportForm(Model model,
                            Authentication authentication);

    @GetMapping({"/{importId}", "/{importId}/"})
    String importPage(@PathVariable("importId") int importId,
                      @RequestParam(name = "page", defaultValue = "1") int page,
                      Model model,
                      Authentication authentication);

    @PostMapping({"/new", "/new/"})
    String createImport(@RequestParam("title") String title,
                        @RequestParam("pageContent") String content,
                        @RequestParam("url") String url,
                        Model model,
                        Authentication authentication);

    @GetMapping({"/url-form", "/url-form/"})
    String urlForm(Model model,
                   Authentication authentication);

    @GetMapping({"/pageContent-form", "/pageContent-form/"})
    String contentForm(Model model,
                       Authentication authentication);
}
