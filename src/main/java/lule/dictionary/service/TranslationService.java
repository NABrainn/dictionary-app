package lule.dictionary.service;

import lombok.RequiredArgsConstructor;
import lule.dictionary.entity.Translation;
import lule.dictionary.enumeration.Familiarity;
import lule.dictionary.repository.TranslationRepository;
import lule.dictionary.exception.RepositoryException;
import lule.dictionary.exception.ServiceException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TranslationService {

    private final TranslationRepository translationRepository;

    public int add(Translation translation, int importId) throws ServiceException {
        try {
            return translationRepository.addTranslation(translation, importId).orElseThrow(() -> new ServiceException("translation belonging to: " + importId + " not found."));
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public List<Translation> findAllByImport(int importId) throws ServiceException{
        try {
            return translationRepository.findAllByImport(importId);
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public Translation findByTargetWord(String targetWord) throws ServiceException{
        try {
            return translationRepository.findByTargetWord(targetWord).orElseThrow(() -> new ServiceException("import for target word: " + targetWord + " not found."));
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public void updateSourceWord(Translation translation, String sourceWord) throws ServiceException{
        try {
            translationRepository.updateSourceWord(translation, sourceWord);
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public void updateFamiliarity(Translation translation, Familiarity familiarity) throws ServiceException{
        try {
            translationRepository.updateFamiliarity(translation, familiarity);
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
    }
}
