package lule.dictionary.service.imports.importService;

import jakarta.validation.Validator;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lule.dictionary.entity.application.implementation.imports.base.DictionaryImport;
import lule.dictionary.service.dto.ServiceResult;
import lule.dictionary.service.imports.importService.dto.AddImportRequest;
import lule.dictionary.entity.application.interfaces.imports.base.Import;
import lule.dictionary.entity.application.interfaces.imports.ImportWithId;
import lule.dictionary.exception.ServiceException;
import lule.dictionary.repository.ImportRepository;
import lule.dictionary.util.errors.ErrorMapFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ImportService {

    private final ImportRepository importRepository;
    private final Validator validator;

    public int addImport(Model model,
                         AddImportRequest addImportRequest) throws ServiceException {
        var constraints = validator.validate(addImportRequest);
        if(!constraints.isEmpty()) {
            model.addAttribute("result", new ServiceResult(true, ErrorMapFactory.fromSet(constraints)));
            throw new ServiceException("Constraints violated at " + addImportRequest);
        }
        try {
            String url = normalizeURL(addImportRequest.url());
            if(addImportRequest.content().isEmpty()) {
                Document document = Jsoup.connect(url).get();
                String content = document.text();
                model.addAttribute("result", new ServiceResult(false, Map.of()));
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
                model.addAttribute("result", new ServiceResult(false, Map.of()));
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
            model.addAttribute("result", new ServiceResult(true, Map.of()));
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
