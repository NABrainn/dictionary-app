package lule.dictionary.service.imports.importService;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lule.dictionary.entity.application.implementation.imports.base.DictionaryImport;
import lule.dictionary.service.imports.importService.dto.AddImportRequest;
import lule.dictionary.entity.application.interfaces.imports.base.Import;
import lule.dictionary.entity.application.interfaces.imports.ImportWithId;
import lule.dictionary.exception.ServiceException;
import lule.dictionary.repository.ImportRepository;
import lule.dictionary.exception.RepositoryException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImportService {

    private final ImportRepository importRepository;

    public int addImport(AddImportRequest addImportRequest) throws ServiceException {
        try {
            String url = normalizeURL(addImportRequest.url());
            if(addImportRequest.content().isEmpty()) {
                Document document = Jsoup.connect(url).get();
                String content = document.text();
                return importRepository.addImport(DictionaryImport.builder()
                            .title(addImportRequest.title())
                            .content(content)
                            .url(addImportRequest.url())
                            .sourceLanguage(addImportRequest.sourceLanguage())
                            .targetLanguage(addImportRequest.targetLanguage())
                            .owner(addImportRequest.owner())
                            .build()).orElseThrow(() -> new ServiceException("Failed to add a new import"));
            }
            else {
                return importRepository.addImport(DictionaryImport.builder()
                        .title(addImportRequest.title())
                        .content(addImportRequest.content())
                        .url(addImportRequest.url())
                        .sourceLanguage(addImportRequest.sourceLanguage())
                        .targetLanguage(addImportRequest.targetLanguage())
                        .owner(addImportRequest.owner())
                        .build()).orElseThrow(() -> new ServiceException("Failed to add a new import"));
            }
        } catch (IOException e) {
            throw new ServiceException("Failed to parse import: " + e.getMessage(), e.getCause());
        }
    }

    public Import findById(int id) throws ServiceException {
        return importRepository.findById(id).orElseThrow(() -> new ServiceException("Failed to fetch import"));
    }
    public void findByOwner(@NonNull Model model, @NonNull String owner) throws ServiceException {
        List<ImportWithId> imports = importRepository.findByOwner(owner);
        model.addAttribute("imports", imports);

    }

    public List<Import> findAll() throws ServiceException {
        return importRepository.findAll();
    }

    private String normalizeURL(String url) {
        if(!url.startsWith("https://") && !url.startsWith("http://")) {
            return "https://".concat(url);
        }
        return url;
    }
}
