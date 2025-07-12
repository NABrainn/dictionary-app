package lule.dictionary.controller.importController;

import jakarta.validation.ConstraintViolationException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.entity.application.interfaces.imports.ImportWithId;
import lule.dictionary.entity.application.interfaces.imports.ImportWithPagination;
import lule.dictionary.entity.application.interfaces.translation.Translation;
import lule.dictionary.entity.application.interfaces.userProfile.CustomUserDetails;
import lule.dictionary.service.imports.exception.ImportNotFoundException;
import lule.dictionary.service.imports.importService.dto.createImportRequest.CreateImportRequestFactory;
import lule.dictionary.service.imports.importService.dto.importData.ImportData;
import lule.dictionary.service.imports.importService.dto.importPageRequest.AssembleImportPageRequest;
import lule.dictionary.service.imports.importService.dto.importPageRequest.ImportPageRequestFactory;
import lule.dictionary.service.imports.importService.dto.importsAttribute.ImportContentAttribute;
import lule.dictionary.service.imports.importService.dto.loadImportPageRequest.LoadImportPageRequest;
import lule.dictionary.service.imports.importService.dto.loadImportPageRequest.LoadImportPageRequestFactory;
import lule.dictionary.service.imports.importService.dto.importData.ImportDataFactory;
import lule.dictionary.service.imports.importService.dto.importsAttribute.ImportsAttributeFactory;
import lule.dictionary.service.imports.importService.ImportService;
import lule.dictionary.service.pagination.PaginationService;
import lule.dictionary.service.pagination.dto.PaginationData;
import lule.dictionary.service.pagination.dto.PaginationDataFactory;
import lule.dictionary.service.translation.TranslationService;
import lule.dictionary.service.translation.dto.attribute.TranslationAttribute;
import lule.dictionary.service.userProfile.exception.UserNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.InvalidUrlException;

import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/imports")
@RequiredArgsConstructor
public class ImportController {

    private final ImportService importService;
    private final TranslationService translationService;
    private final PaginationService paginationService;
    private final PaginationDataFactory paginationDataFactory;
    private final ImportDataFactory importDataFactory;
    private final ImportsAttributeFactory importsAttributeFactory;
    private final ImportPageRequestFactory importPageRequestFactory;
    private final LoadImportPageRequestFactory loadImportPageRequestFactory;
    private final CreateImportRequestFactory createImportRequestFactory;

    @GetMapping("")
    public String getImportList(Authentication authentication,
                                Model model) {
        List<ImportWithId> imports = getImports(authentication, model);
        model.addAttribute("imports", imports);
        return "imports";
    }

    @PostMapping({"/page/reload", "/page/reload/"})
    public String reloadImportPageOnPost(@RequestAttribute("translationAttribute") TranslationAttribute translationAttribute,
                                         @RequestParam("selectedWordId") int wordId,
                                         @RequestParam("importId") int importId,
                                         @RequestParam("page") int page,
                                         Model model) {
        try {
            ImportContentAttribute importContentAttribute = loadImportPage(loadImportPageRequestFactory.of(wordId, importId, page));
            model.addAttribute("importContentAttribute", importContentAttribute);
            model.addAttribute("translationAttribute", translationAttribute);
            return "import-page/content";
        }
        catch (InvalidUrlException | ImportNotFoundException e) {
            log.warn("reloadImportPageOnPost(): Sending to error page due to invalid url or missing import: {}", e.getMessage());
            return "error";
        }

    }

    @PutMapping({"/page/reload", "/page/reload/"})
    public String reloadImportPageOnPut(@RequestAttribute("translationAttribute") TranslationAttribute translationAttribute,
                                        @RequestParam("selectedWordId") int wordId,
                                        @RequestParam("importId") int importId,
                                        @RequestParam("page") int page,
                                        Model model) {
        try {
            ImportContentAttribute importContentAttribute = loadImportPage(loadImportPageRequestFactory.of(wordId, importId, page));
            model.addAttribute("importContentAttribute", importContentAttribute);
            model.addAttribute("translationAttribute", translationAttribute);
            return "import-page/content";
        }
        catch (InvalidUrlException | ImportNotFoundException e) {
            log.warn("reloadImportPageOnPut(): Sending to error page due to invalid url or missing import: {}", e.getMessage());
            return "error";
        }
    }

    @GetMapping({"/new", "/new/"})
    public String addImportForm() {
        return "import-form/import-form";
    }

    @GetMapping({"/{importId}", "/{importId}/"})
    public String getImportPageContent(@PathVariable("importId") int importId,
                                       @RequestParam(name = "page", defaultValue = "1") int page,
                                       Model model) {
        try {
            ImportContentAttribute importContentAttribute = loadImportPage(loadImportPageRequestFactory.of(0, importId, page));
            model.addAttribute("importContentAttribute", importContentAttribute);
            model.addAttribute("translationAttribute", null);
            return "import-page/import-page";

        }
        catch (InvalidUrlException | ImportNotFoundException e) {
            log.warn("getImportPageContent(): Sending to error page due to invalid url or missing import: {}", e.getMessage());
            return "error";
        }
    }

    @PostMapping({"/new", "/new/"})
    public String createImport(@RequestParam("title") String title,
                               @RequestParam("content") String content,
                               @RequestParam("url") String url,
                                Model model,
                                Authentication authentication) {
        try {
            int importId = importService.createImport(createImportRequestFactory.of(title, content, url, extractUsername(authentication)));
            model.addAttribute("result");
            return "redirect:/imports/" + importId + "?page=1";
        } catch (UserNotFoundException e) {
            log.info("Redirecting to login page due to user not found: {}", e.getMessage());
            return "redirect:/auth/login";

        } catch (ConstraintViolationException e) {
            log.warn("Retrying view due to input issue: {}", e.getMessage());
            return "import-form/import-form";
        }
    }

    @GetMapping({"/by-url-form", "/by-url-form/"})
    public String byUrlForm() {
        return "import-form/by-url-form";
    }

    @GetMapping({"/insert-manually-form", "/insert-manually-form/"})
    public String insertManuallyForm() {
        return "import-form/insert-manually-form";
    }


    private @NonNull String extractUsername(Authentication authentication) {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        return principal.getUsername();
    }

    private ImportContentAttribute loadImportPage(LoadImportPageRequest loadRequest) {
        ImportWithPagination importWithPagination = getImportPage(loadRequest);
        return assembleImportPageAttribute(importPageRequestFactory.of(loadRequest.wordId(), loadRequest.importId(), loadRequest.page(), importWithPagination, getTotalLength(importWithPagination)));
    }

    private List<ImportWithId> getImports(Authentication authentication, Model model) {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        return importService.findByOwner(model, principal.getUsername());
    }

    private int getTotalLength(ImportWithPagination importWithPagination) {
        return importWithPagination.content().length();
    }

    private ImportContentAttribute assembleImportPageAttribute(AssembleImportPageRequest assembleRequest) {
        ImportData importData = createImportData(assembleRequest.wordId(), assembleRequest.importId(), assembleRequest.importWithPagination());
        PaginationData paginationData = createPaginationData(assembleRequest.page(), getNumberOfPages(assembleRequest.totalLength()));
        return createImportPageAttribute(importData, paginationData);
    }

    private ImportContentAttribute createImportPageAttribute(ImportData importData, PaginationData paginationData) {
        return importsAttributeFactory.of(importData, paginationData);
    }

    private int getNumberOfPages(int length) {
        return paginationService.getNumberOfPages(length);
    }

    private ImportWithPagination getImportPage(LoadImportPageRequest loadRequest) {
            return importService.loadPage(loadRequest);
    }

    private PaginationData createPaginationData(int page, int pagesTotal) {
        return paginationDataFactory.of(page, pagesTotal, getRows(pagesTotal), getNumberOfCurrentRow(page), getNumberOfRowFirstPage(page));
    }

    private ImportData createImportData(int wordId, int importId, ImportWithPagination importWithPagination) {
        return importDataFactory.of(getImportTitle(importWithPagination), getImportContent(importWithPagination), getImportTranslations(importWithPagination), importId, wordId);
    }

    private Map<String, Translation> getImportTranslations(ImportWithPagination importWithPagination) {
        return translationService.findTranslationsByImport(importWithPagination, importWithPagination.owner());
    }

    private List<String> getImportContent(ImportWithPagination importWithPagination) {
        return List.of(importWithPagination.pageContent().split("[ \\n]+"));
    }

    private String getImportTitle(ImportWithPagination importWithPagination) {
        return importWithPagination.title();
    }

    private int getNumberOfRowFirstPage(int page) {
        return paginationService.getFirstPageOfRow(page, paginationService.getMAX_ROW_SIZE());
    }

    private int getNumberOfCurrentRow(int page) {
        return paginationService.getCurrentRow(page, paginationService.getMAX_ROW_SIZE());
    }

    private List<List<Integer>> getRows(int pagesTotal) {
        return paginationService.getRows(pagesTotal);
    }
}
