package lule.dictionary.service.translation.exception;

import lule.dictionary.exception.application.ServiceException;
import lule.dictionary.dto.application.result.ServiceResult;

public class TranslationNotFoundException extends ServiceException {
    public TranslationNotFoundException(ServiceResult<?> ob) {
        super(ob);
    }
}
