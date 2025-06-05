package lule.dictionary.service.application.entity;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lule.dictionary.dto.application.interfaces.translation.Translation;
import lule.dictionary.enumeration.Familiarity;
import lule.dictionary.exception.ResourceNotFoundException;
import lule.dictionary.repository.TranslationRepository;
import lule.dictionary.exception.RepositoryException;
import lule.dictionary.exception.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TranslationService {

    private final TranslationRepository translationRepository;

    public int add(@NonNull Translation translation, int importId) throws ServiceException {
        try {
            return translationRepository.addTranslation(translation.translationDetails(), translation.userProfileSettings(), translation.owner(), importId).orElseThrow(() -> new ServiceException("Failed to add new translation"));
        } catch (RepositoryException e) {
            throw new ServiceException("Failed to add new translation", e.getCause());
        }
    }

    public List<Translation> findAllByOwner(@NonNull String owner) throws ServiceException{
        try {
            return translationRepository.findByOwner(owner);
        } catch (RepositoryException e) {
            throw new ServiceException("Failed to fetch translations", e.getCause());
        }
    }

    public Translation findByTargetWord(@NonNull String targetWord) throws ServiceException{
        try {
            if(targetWord.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "String is not in a valid state");
            return translationRepository.findByTargetWord(targetWord).orElseThrow(() -> new ResourceNotFoundException("Failed to fetch translation for word " + targetWord));
        } catch (RepositoryException e) {
            throw new ServiceException("Failed to fetch translation for word " + targetWord, e.getCause());
        }
    }

    public void updateSourceWord(@NonNull String sourceWord, @NonNull String targetWord) throws ServiceException{
        try {
            translationRepository.updateSourceWord(sourceWord, targetWord).orElseThrow(() -> new ServiceException("Failed to update source word for " + targetWord));
        } catch (RepositoryException e) {
            throw new ServiceException("Failed to update source word for " + targetWord, e.getCause());
        }
    }

    public void updateFamiliarity(@NonNull String targetWord, @NonNull Familiarity familiarity) throws ServiceException{
        try {
            translationRepository.updateFamiliarity(targetWord, familiarity).orElseThrow(() -> new ServiceException("Failed to update familiarity for " + targetWord));
        } catch (RepositoryException e) {
            throw new ServiceException("Failed to update familiarity", e.getCause());
        }
    }

    public List<Translation> findByTargetWords(List<String> targetWords) {
        try {
            return translationRepository.findByTargetWords(targetWords);
        } catch (RepositoryException e) {
            throw new ServiceException("Failed to fetch translations", e.getCause());
        }
    }

    public int getFamiliarityAsInt(Familiarity familiarity) {
        return switch (familiarity) {
            case UNKNOWN -> 1;
            case RECOGNIZED -> 2;
            case FAMILIAR -> 3;
            case KNOWN -> 4;
            case IGNORED -> 5;
        };
    }
}
