package lule.dictionary.service.translation.exception;

import lule.dictionary.service.dto.exception.ServiceException;
import lule.dictionary.service.dto.result.ServiceResult;

public class TranslationNotFoundException extends ServiceException {
    public TranslationNotFoundException(ServiceResult<?> ob) {
        super(ob);
    }
}
