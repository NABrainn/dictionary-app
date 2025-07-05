package lule.dictionary.service.dto;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ServiceResultFactory {

    public ServiceResult createErrorResult(Map<String, String> messages) {
        return new ServiceResult(true, messages);
    }

    public ServiceResult createSuccessResult(Map<String, String> messages) {
        return new ServiceResult(false, messages);
    }
}