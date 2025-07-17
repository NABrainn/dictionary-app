package lule.dictionary.controller.importController;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.dto.application.ImportContentData;
import lule.dictionary.dto.database.interfaces.imports.ImportWithId;
import lule.dictionary.dto.database.interfaces.imports.ImportWithPagination;
import lule.dictionary.dto.database.interfaces.translation.Translation;
import lule.dictionary.dto.database.interfaces.userProfile.CustomUserDetails;
import lule.dictionary.exception.application.InvalidInputException;
import lule.dictionary.dto.application.result.ServiceResult;
import lule.dictionary.service.imports.exception.ImportNotFoundException;
import lule.dictionary.service.imports.importService.dto.createImportRequest.CreateImportRequest;
import lule.dictionary.service.imports.importService.dto.importData.ImportData;
import lule.dictionary.service.imports.importService.dto.importPageRequest.AssembleImportContentRequest;
import lule.dictionary.service.imports.importService.dto.importsAttribute.ImportContentAttribute;
import lule.dictionary.service.imports.importService.dto.loadImportPageRequest.LoadImportPageRequest;
import lule.dictionary.service.imports.importService.ImportServiceImp;
import lule.dictionary.service.pagination.PaginationService;
import lule.dictionary.service.pagination.dto.PaginationData;
import lule.dictionary.service.translation.TranslationService;
import lule.dictionary.service.translation.dto.attribute.TranslationAttribute;
import lule.dictionary.service.userProfile.exception.UserNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.InvalidUrlException;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Slf4j
@Controller
@RequestMapping("/imports")
@RequiredArgsConstructor
public class ImportControllerImp implements ImportController {

    private final ImportServiceImp importService;
    private final TranslationService translationService;
    private final PaginationService paginationService;

    @GetMapping("")
    public String importListPage(Authentication authentication,
                                 Model model) {
        List<ImportWithId> imports = getImports(authentication);
        model.addAttribute("imports", imports);
        return "imports";
    }

    @PostMapping({"/page/reload", "/page/reload/"})
    public String importPageOnPost(@RequestAttribute("translationAttribute") TranslationAttribute translationAttribute,
                                   @RequestParam("selectedWordId") int wordId,
                                   @RequestParam("importId") int importId,
                                   @RequestParam("page") int page,
                                   Model model) {
        try {
            ImportContentAttribute importContentAttribute = loadImportPage(LoadImportPageRequest.of(wordId, importId, page));
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
    public String importPageOnPut(@RequestAttribute("translationAttribute") TranslationAttribute translationAttribute,
                                  @RequestParam("selectedWordId") int wordId,
                                  @RequestParam("importId") int importId,
                                  @RequestParam("page") int page,
                                  Model model) {
        try {
            ImportContentAttribute importContentAttribute = loadImportPage(LoadImportPageRequest.of(wordId, importId, page));
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
    public String createImportForm() {
        return "import-form/import-form";
    }

    @GetMapping({"/{importId}", "/{importId}/"})
    public String importPage(@PathVariable("importId") int importId,
                                       @RequestParam(name = "page", defaultValue = "1") int page,
                                       Model model) {
        try {
            ImportContentAttribute importContentAttribute = loadImportPage(LoadImportPageRequest.of(0, importId, page));
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
            ServiceResult<Integer> result = importService.createImport(CreateImportRequest.of(title, content, url, extractUsername(authentication)));
            model.addAttribute("result", result);
            return "redirect:/imports/" + result.value() + "?page=1";
        } catch (UserNotFoundException e) {
            log.info("Redirecting to login page due to user not found: {}", e.getMessage());
            return "redirect:/auth/login";

        } catch (InvalidInputException e) {
            log.warn("Retrying view due to input issue: {}", e.getMessage());
            model.addAttribute("result", e.getResult());
            return "import-form/import-form";
        }
    }

    @GetMapping({"/url-form", "/url-form/"})
    public String urlForm() {
        return "import-form/url-form";
    }

    @GetMapping({"/textarea-form", "/textarea-form/"})
    public String textareaForm() {
        return "import-form/textarea-form";
    }


    private @NonNull String extractUsername(Authentication authentication) {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        return principal.getUsername();
    }

    private ImportContentAttribute loadImportPage(LoadImportPageRequest loadRequest) {
        ImportWithPagination importWithPagination = getImportPage(loadRequest);
        return assembleImportContentAttribute(AssembleImportContentRequest.builder()
                .wordId(loadRequest.wordId())
                .importId(loadRequest.importId())
                .page(loadRequest.page())
                .importWithPagination(importWithPagination)
                .totalLength(getTotalLength(importWithPagination))
                .build());

    }

    private List<ImportWithId> getImports(Authentication authentication) {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        return importService.findByOwnerAndTargetLanguage(principal.getUsername(), principal.targetLanguage()).value();
    }

    private int getTotalLength(ImportWithPagination importWithPagination) {
        return importWithPagination.content().length();
    }

    private ImportContentAttribute assembleImportContentAttribute(AssembleImportContentRequest assembleRequest) {
        ImportData importData = createImportData(assembleRequest.wordId(), assembleRequest.importId(), assembleRequest.importWithPagination());
        PaginationData paginationData = createPaginationData(assembleRequest.page(), getNumberOfPages(assembleRequest.totalLength()));
        return createImportContentAttribute(importData, paginationData);
    }

    private ImportContentAttribute createImportContentAttribute(ImportData importData, PaginationData paginationData) {
        return ImportContentAttribute.of(importData, paginationData);
    }

    private int getNumberOfPages(int length) {
        return paginationService.getNumberOfPages(length);
    }

    private ImportWithPagination getImportPage(LoadImportPageRequest loadRequest) {
        return importService.loadPage(loadRequest).value();
    }

    private PaginationData createPaginationData(int page, int pagesTotal) {
        return PaginationData.builder()
                .currentPageNumber(page)
                .numberOfPages(pagesTotal)
                .currentRowNumber(getNumberOfCurrentRow(page))
                .firstPageOfRowNumber(getNumberOfRowFirstPage(page))
                .rows(getRows(pagesTotal))
                .build();
    }

    private ImportData createImportData(int wordId, int importId, ImportWithPagination importWithPagination) {
        return ImportData.builder()
                .selectedWordId(wordId)
                .importId(importId)
                .title(getImportTitle(importWithPagination))
                .content(getImportContent(importWithPagination))
                .translations(getImportTranslations(importWithPagination))
                .build();
    }

    private Map<String, Translation> getImportTranslations(ImportWithPagination importWithPagination) {
        return translationService.findTranslationsByImport(importWithPagination, importWithPagination.owner()).value();
    }

    private ImportContentData getImportContent(ImportWithPagination importWithPagination) {
        List<List<String>> paragraphs = Stream.of(importWithPagination.pageContent().split("\n+"))
                .map(paragraph -> Arrays.stream(paragraph.split("\\s+")).toList())
                .filter(list -> !list.isEmpty())
                .toList();
        List<Integer> startIndices = IntStream.range(0, paragraphs.size())
                .map(i -> 1 + paragraphs.subList(0, i).stream().mapToInt(List::size).sum())
                .boxed()
                .toList();
        return new ImportContentData(paragraphs, startIndices);
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
