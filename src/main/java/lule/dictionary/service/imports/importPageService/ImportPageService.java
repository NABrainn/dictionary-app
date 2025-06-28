package lule.dictionary.service.imports.importPageService;

import lombok.RequiredArgsConstructor;
import lule.dictionary.entity.application.interfaces.imports.ImportWithPagination;
import lule.dictionary.service.imports.exception.ImportNotFoundException;
import lule.dictionary.service.imports.importPageService.dto.ImportModel;
import lule.dictionary.service.imports.importPageService.dto.LoadImportRequest;
import lule.dictionary.service.imports.importService.ImportService;
import lule.dictionary.service.pagination.PaginationService;
import lule.dictionary.service.translation.TranslationService;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.util.InvalidUrlException;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ImportPageService {

    private final ImportService importService;
    private final TranslationService translationService;
    private final PaginationService paginationService;

    public void loadImportWithTranslations(Model model, LoadImportRequest request) {
        try {
            ImportWithPagination imported = importService.findById(request.importId(), request.page());
            int pagesTotal = (int) Math.ceil((double) imported.content().length() / 2000);
            if(request.page() <= 0 || request.page() > pagesTotal) {
                throw new InvalidUrlException("Invalid url parameter provided");
            }
            model.addAttribute("importModel", new ImportModel(
                    imported.title(),
                    List.of(imported.pageContent().split("[ \\n]+")),
                    translationService.findTranslationsByImport(imported),
                    request.importId(),
                    request.wordId(),
                    request.page(),
                    pagesTotal,
                    paginationService.getRows(pagesTotal),
                    paginationService.getCurrentRow(request.page(), paginationService.getMaxRowSize()),
                    paginationService.getFirstPageOfRow(request.page(), paginationService.getMaxRowSize()))
            );
            model.addAttribute("translationModel", request.translationModel());
        } catch (NullPointerException | ImportNotFoundException e) {
            model.addAttribute("translationModel", null);
        }
    }
}
