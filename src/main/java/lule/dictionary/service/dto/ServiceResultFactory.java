package lule.dictionary.service.dto;

import java.util.Map;

public class ServiceResultFactory {

    public static ServiceResult createErrorResult(Map<String, String> messages) {
        return new ServiceResult(true, messages);
    }

    public static ServiceResult createSuccessResult(Map<String, String> messages) {
        return new ServiceResult(false, messages);
    }
}