package lule.dictionary.service.jsoup.exception;

import lule.dictionary.dto.application.result.ServiceResult;
import lule.dictionary.exception.application.ServiceException;

public class InvalidUriException extends ServiceException {
    public InvalidUriException(String message, ServiceResult<?> ob) {
        super(message, ob);
    }

    public InvalidUriException(ServiceResult<?> ob) {
        super(ob);
    }
}
