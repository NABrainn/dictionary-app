package lule.dictionary.service.dto.exception;

import lombok.Getter;
import lule.dictionary.service.dto.result.ServiceResult;

@Getter
public class ServiceException extends RuntimeException {

    private final ServiceResult<?> result;

    public ServiceException(String message, ServiceResult<?> ob) {
        super(message);
        this.result = ob;
    }

    public ServiceException(ServiceResult<?> ob) {
        this.result = ob;
    }
}
