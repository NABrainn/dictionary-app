package lule.dictionary.service.application.dto;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lule.dictionary.dto.application.interfaces.imports.Import;
import lule.dictionary.exception.ServiceException;
import lule.dictionary.repository.ImportRepository;
import lule.dictionary.exception.RepositoryException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImportService {

    private final ImportRepository importRepository;

    public int addImport(@NonNull Import imported) throws ServiceException {
        try {
            return importRepository.addImport(imported.importDetails(), imported.userProfileSettings(), imported.owner()).orElseThrow(() -> new ServiceException("Failed to add a new import"));
        } catch (RepositoryException e) {
            throw new ServiceException("Failed to add a new import");
        }
    }

    public Import findById(int id) throws ServiceException {
        try {
            return importRepository.findById(id).orElseThrow(() -> new ServiceException("Failed to fetch import"));
        } catch (RepositoryException e) {
            throw new ServiceException("Failed to fetch import");
        }
    }
    public List<Import> findByOwner(@NonNull String owner) throws ServiceException {
        try {
            return importRepository.findByOwner(owner);
        } catch (RepositoryException e) {
            throw new ServiceException("Failed to fetch imports");
        }
    }

    public List<Import> findAll() throws ServiceException {
        try {
            return importRepository.findAll();
        } catch (RepositoryException e) {
            throw new ServiceException("Failed to fetch imports");
        }
    }
}
