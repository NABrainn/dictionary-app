package lule.dictionary.exception.application;

import lule.dictionary.dto.application.result.ServiceResult;


public class InvalidInputException extends ServiceException {
    public InvalidInputException(ServiceResult<?> ob) {
        super(ob);
    }
    public InvalidInputException(String message, ServiceResult<?> ob) {
        super(message, ob);
    }
}
