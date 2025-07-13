package lule.dictionary.dto.application.result;

import java.util.Map;

public interface ServiceResult<T> {
    T value();
    boolean hasError();
    Map<String, String> messages();
}
