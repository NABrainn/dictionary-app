package lule.dictionary.service.auth.dto.result.dto;

import lule.dictionary.service.dto.result.ServiceResult;

import java.util.Map;

public record SignupResult(Object value, boolean isError, Map<String, String> messages) implements ServiceResult<Object> {
    @Override
    public Object value() {
        if(value == null) throw new NullPointerException("Value not expected");
        return null;
    }

    @Override
    public Map<String, String> messages() {
        return Map.copyOf(messages);
    }
}
