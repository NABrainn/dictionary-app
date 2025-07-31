package lule.dictionary.dto.application.result;

import lombok.NonNull;

import java.util.Collections;
import java.util.Map;

public record ServiceResultImp<T>(T value,
                                  boolean hasError,
                                  @NonNull Map<String, String> messages) implements ServiceResult<T> {

    @Override
    public T value() {
        if(value == null) throw new NullPointerException("Illegal read operation: value is null");
        return value;
    }

    @Override
    public Map<String, String> messages() {
        return Collections.unmodifiableMap(messages);
    }

    public static <T> ServiceResultImp<T> success(T value) {
        return new ServiceResultImp<>(value, false, Map.of());
    }

    public static <T> ServiceResultImp<T> success(T value, Map<String, String> messages) {
        return new ServiceResultImp<>(value, false, messages);
    }

    public static <T> ServiceResultImp<T> success(Map<String, String> messages) {
        return new ServiceResultImp<>(null, false, messages);
    }

    public static <T> ServiceResultImp<T> error(T value) {
        return new ServiceResultImp<>(value, true, Map.of());
    }

    public static <T> ServiceResultImp<T> error(T value, Map<String, String> messages) {
        return new ServiceResultImp<>(value, true, messages);
    }

    public static <T> ServiceResultImp<T> error(Map<String, String> messages) {
        return new ServiceResultImp<>(null, true, messages);
    }

}
