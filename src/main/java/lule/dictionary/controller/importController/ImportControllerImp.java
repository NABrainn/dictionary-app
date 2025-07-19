package lule.dictionary.controller.importController;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.dto.application.ContentData;
import lule.dictionary.dto.database.interfaces.imports.ImportWithId;
import lule.dictionary.dto.database.interfaces.imports.ImportWithPagination;
import lule.dictionary.dto.database.interfaces.translation.Translation;
import lule.dictionary.dto.database.interfaces.userProfile.CustomUserDetails;
import lule.dictionary.exception.application.InvalidInputException;
import lule.dictionary.dto.application.result.ServiceResult;
import lule.dictionary.service.imports.exception.ImportNotFoundException;
import lule.dictionary.service.imports.importService.dto.request.*;
import lule.dictionary.service.imports.importService.dto.importData.ImportAttribute;
import lule.dictionary.service.imports.importService.ImportServiceImp;
import lule.dictionary.service.language.Language;
import lule.dictionary.service.localization.LocalizationService;
import lule.dictionary.service.pagination.PaginationService;
import lule.dictionary.service.pagination.dto.PaginationData;
import lule.dictionary.service.translation.TranslationService;
import lule.dictionary.service.translation.dto.attribute.TranslationAttribute;
import lule.dictionary.service.translation.dto.request.FindTranslationsByImportRequest;
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
    private final LocalizationService localizationService;

    @GetMapping("")
    public String importListPage(Authentication authentication,
                                 Model model) {

        List<ImportWithId> imports = getImports(authentication);
        System.out.println("list: " + imports);
        model.addAttribute("navbarLocalization", localizationService.navbarLocalization(Language.EN));
        model.addAttribute("imports", imports);
        return "document-list-page/documents";
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
            model.addAttribute("navbarLocalization", localizationService.navbarLocalization(Language.EN));
            return "document-page/content/content";
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
            model.addAttribute("navbarLocalization", localizationService.navbarLocalization(Language.EN));
            return "document-page/content/content";
        }
        catch (InvalidUrlException | ImportNotFoundException e) {
            log.warn("reloadImportPageOnPut(): Sending to error page due to invalid url or missing import: {}", e.getMessage());
            return "error";
        }
    }

    @GetMapping({"/new", "/new/"})
    public String createImportForm(Model model) {
        model.addAttribute("navbarLocalization", localizationService.navbarLocalization(Language.EN));
        return "create-import-form/base-form";
    }

    @GetMapping({"/{importId}", "/{importId}/"})
    public String importPage(@PathVariable("importId") int importId,
                                       @RequestParam(name = "page", defaultValue = "1") int page,
                                       Model model) {
        try {
            ImportContentAttribute importContentAttribute = loadImportPage(LoadImportPageRequest.of(0, importId, page));
            model.addAttribute("importContentAttribute", importContentAttribute);
            model.addAttribute("translationAttribute", null);
            return "document-page/base-page";

        }
        catch (InvalidUrlException e) {
            log.warn("Invalid url: {}", e.getMessage());
            return "error";
        }
        catch (ImportNotFoundException e) {
            log.warn("Import not found: {}", e.getMessage());
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
            model.addAttribute("navbarLocalization", localizationService.navbarLocalization(Language.EN));
            return "redirect:/imports/" + result.value() + "?page=1";
        } catch (UserNotFoundException e) {
            log.info("Redirecting to login page due to user not found: {}", e.getMessage());
            return "redirect:/auth/login";

        } catch (InvalidInputException e) {
            log.warn("Retrying view due to input issue: {}", e.getMessage());
            model.addAttribute("result", e.getResult());
            model.addAttribute("navbarLocalization", localizationService.navbarLocalization(Language.EN));
            return "create-import-form/base-form";
        }
    }

    @GetMapping({"/url-form", "/url-form/"})
    public String urlForm() {
        return "create-import-form/url-form";
    }

    @GetMapping({"/textarea-form", "/textarea-form/"})
    public String contentForm() {
        return "create-import-form/textarea-form";
    }


    private @NonNull String extractUsername(Authentication authentication) {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        return principal.getUsername();
    }

    private ImportContentAttribute loadImportPage(LoadImportPageRequest loadRequest) {
        ImportWithPagination importWithPagination = getImportPage(loadRequest);
        AssembleImportContentRequest request = AssembleImportContentRequest.builder()
                .wordId(loadRequest.wordId())
                .importId(loadRequest.importId())
                .page(loadRequest.page())
                .importWithPagination(importWithPagination)
                .totalLength(getTotalLength(importWithPagination))
                .build();
        return assembleImportContentAttribute(request);

    }

    private List<ImportWithId> getImports(Authentication authentication) {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        return importService.findByOwnerAndTargetLanguage(FindByOwnerAndTargetLanguageRequest.of(principal.getUsername(), principal.targetLanguage())).value();
    }

    private int getTotalLength(ImportWithPagination importWithPagination) {
        return importWithPagination.totalContentLength();
    }

    private ImportContentAttribute assembleImportContentAttribute(AssembleImportContentRequest request) {
        ImportAttribute importAttribute = createImportData(request);
        PaginationData paginationData = createPaginationData(request);
        return createImportContentAttribute(importAttribute, paginationData);
    }

    private ImportContentAttribute createImportContentAttribute(ImportAttribute importData, PaginationData paginationData) {
        return ImportContentAttribute.of(importData, paginationData);
    }

    private int getNumberOfPages(int length) {
        return paginationService.getNumberOfPages(length);
    }

    private ImportWithPagination getImportPage(LoadImportPageRequest loadRequest) {
        return importService.loadPage(loadRequest).value();
    }

    private PaginationData createPaginationData(AssembleImportContentRequest request) {
        int currentPage = request.page();
        int pagesTotal = getNumberOfPages(request.totalLength());
        return PaginationData.builder()
                .currentPageNumber(currentPage)
                .numberOfPages(pagesTotal)
                .currentRowNumber(getNumberOfCurrentRow(currentPage))
                .firstPageOfRowNumber(getNumberOfRowFirstPage(currentPage))
                .rows(getRows(pagesTotal))
                .build();
    }

    private ImportAttribute createImportData(AssembleImportContentRequest request) {
        ContentData importContentData = assembleImportContentData(request.importWithPagination());
        Map<String, Translation> importTranslations = getImportTranslationsFromDatabase(request.importWithPagination());
        return ImportAttribute.builder()
                .selectedWordId(request.wordId())
                .importId(request.importId())
                .title(request.importWithPagination().title())
                .content(importContentData)
                .translations(importTranslations)
                .build();
    }

    private Map<String, Translation> getImportTranslationsFromDatabase(ImportWithPagination importWithPagination) {
        return translationService.findTranslationsByImport(FindTranslationsByImportRequest.of(importWithPagination, importWithPagination.owner())).value();
    }

    private ContentData assembleImportContentData(ImportWithPagination importWithPagination) {
        List<List<String>> paragraphs = extractParagraphs(importWithPagination);
        List<Integer> startIndices = extractIndices(paragraphs);
        return ContentData.of(paragraphs, startIndices);
    }

    private List<Integer> extractIndices(List<List<String>> paragraphs) {
        return IntStream.range(0, paragraphs.size())
                .map(i -> 1 + paragraphs.subList(0, i).stream().mapToInt(List::size).sum())
                .boxed()
                .toList();
    }

    private List<List<String>> extractParagraphs(ImportWithPagination importWithPagination) {
        return Stream.of(importWithPagination.pageContent().split("\n+"))
                .map(paragraph -> Arrays.stream(paragraph.split("\\s+")).toList())
                .filter(list -> !list.isEmpty())
                .toList();
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
