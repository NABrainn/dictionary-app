package lule.dictionary.service;

import lombok.RequiredArgsConstructor;
import lule.dictionary.entity.Import;
import lule.dictionary.repository.ImportRepository;
import lule.dictionary.repository.exception.RepositoryException;
import lule.dictionary.service.exception.ServiceException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImportService {

    private final ImportRepository importRepository;

    public int add(Import imported) throws ServiceException{
        try {
            return importRepository.addImport(imported).orElseThrow(() -> new RuntimeException("import not found"));
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public void deleteById(int id) throws ServiceException{
        try {
            importRepository.deleteById(id);
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public List<Import> findAllByOwner(String owner) throws ServiceException {
        try {
            return importRepository.findAllByOwner(owner);
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
    }
}
