package lule.dictionary.auth.data.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Getter
@RequiredArgsConstructor
public class AuthServiceException extends RuntimeException {
    private final Map<String, String> violation;
}
