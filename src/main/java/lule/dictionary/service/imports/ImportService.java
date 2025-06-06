package lule.dictionary.service.imports;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lule.dictionary.controller.imports.dto.AddImportRequest;
import lule.dictionary.dto.application.interfaces.imports.Import;
import lule.dictionary.exception.ServiceException;
import lule.dictionary.factory.dto.ImportFactory;
import lule.dictionary.repository.ImportRepository;
import lule.dictionary.exception.RepositoryException;
import lule.dictionary.service.DocumentParsingService;
import lule.dictionary.service.StringParsingService;
import lule.dictionary.validation.implementation.UrlValidationStrategy;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImportService {

    private final ImportRepository importRepository;
    private final UrlValidationStrategy urlValidationStrategy;
    private final DocumentParsingService contentParser;
    private final StringParsingService stringParsingService;

    public int addImport(@NonNull Model model, AddImportRequest addImportRequest) throws ServiceException {
        try {
            if(!addImportRequest.url().startsWith("https://") || !addImportRequest.url().startsWith("http://")) {
                String urlWithHttps = "https://".concat(addImportRequest.url());
                boolean result = urlValidationStrategy.validate(urlWithHttps);
                if (result) {
                    Document document = contentParser.fetchContent(urlWithHttps);
                    String content = document.text();
                    int importId =  importRepository.addImport(ImportFactory.createImportDetails(
                            addImportRequest.title(),
                            content,
                            addImportRequest.url()
                    ), addImportRequest.userProfileSettings(), addImportRequest.owner()).orElseThrow(() -> new ServiceException("Failed to add a new import"));
                    return importId;
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
    public List<Import> findByOwner(@NonNull String owner) throws ServiceException {
        try {
            return importRepository.findByOwner(owner);
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
