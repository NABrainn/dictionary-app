package lule.dictionary.service.dto.exception;

import lule.dictionary.service.dto.result.ServiceResult;

public class InvalidInputException extends ServiceException {
    public InvalidInputException(ServiceResult<?> ob) {
        super(ob);
    }
    public InvalidInputException(String message, ServiceResult<?> ob) {
        super(message, ob);
    }
}
