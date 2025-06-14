package lule.dictionary.service.imports;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lule.dictionary.controller.catalog.dto.AddImportRequest;
import lule.dictionary.controller.catalog.dto.ImportPageModel;
import lule.dictionary.controller.catalog.dto.SaveTranslationRequest;
import lule.dictionary.dto.application.interfaces.imports.Import;
import lule.dictionary.dto.application.interfaces.translation.Translation;
import lule.dictionary.enumeration.Language;
import lule.dictionary.exception.ServiceException;
import lule.dictionary.factory.dto.ImportFactory;
import lule.dictionary.repository.ImportRepository;
import lule.dictionary.exception.RepositoryException;
import lule.dictionary.service.DocumentParsingService;
import lule.dictionary.service.auth.validator.exception.ValidationStrategyException;
import lule.dictionary.service.imports.validator.UrlValidator;
import org.jsoup.nodes.Document;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImportService {

    private final ImportRepository importRepository;
    private final UrlValidator urlValidator;
    private final DocumentParsingService contentParser;

    public int addImport(Authentication authentication, @NonNull Model model, AddImportRequest addImportRequest) throws ServiceException {
        try {
            if(!addImportRequest.url().startsWith("https://") || !addImportRequest.url().startsWith("http://")) {
                String urlWithHttps = "https://".concat(addImportRequest.url());
                try {
                    if(addImportRequest.content().isEmpty()) {
                        urlValidator.validate(urlWithHttps);
                        Document document = contentParser.fetchContent(urlWithHttps);
                        String content = document.text();
                        int importId =  importRepository.addImport(ImportFactory.createImportDetails(
                                addImportRequest.title(),
                                content,
                                addImportRequest.url()
                        ), addImportRequest.userProfileSettings(), addImportRequest.owner()).orElseThrow(() -> new ServiceException("Failed to add a new import"));
                        model.addAttribute("authentication", authentication);
                        return importId;
                    }
                    else {
                        int importId =  importRepository.addImport(ImportFactory.createImportDetails(
                                addImportRequest.title(),
                                addImportRequest.content(),
                                addImportRequest.url()
                        ), addImportRequest.userProfileSettings(), addImportRequest.owner()).orElseThrow(() -> new ServiceException("Failed to add a new import"));
                        model.addAttribute("authentication", authentication);
                        return importId;
                    }
                } catch (ValidationStrategyException e) {
                    throw new ServiceException(e.getMessage());
                }
            }
            throw new ServiceException("Invalid url");
        } catch (RepositoryException | IOException e) {
            throw new ServiceException("Failed to add a new import", e.getCause());
        }
    }



    public Import findById(int id) throws ServiceException {
        try {
            return importRepository.findById(id).orElseThrow(() -> new ServiceException("Failed to fetch import"));
        } catch (RepositoryException e) {
            throw new ServiceException("Failed to fetch import", e.getCause());
        }
    }
    public List<Import> findByOwner(Authentication authentication, @NonNull Model model, @NonNull String owner) throws ServiceException {
        try {
            List<Import> imports = importRepository.findByOwner(owner);
            model.addAttribute("imports", imports);
            return imports;
        } catch (RepositoryException e) {
            throw new ServiceException("Failed to fetch imports", e.getCause());
        }
    }

    public List<Import> findAll() throws ServiceException {
        try {
            return importRepository.findAll();
        } catch (RepositoryException e) {
            throw new ServiceException("Failed to fetch imports", e.getCause());
        }
    }
}
