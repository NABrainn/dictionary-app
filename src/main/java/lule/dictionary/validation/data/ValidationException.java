package lule.dictionary.validation.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
@Getter
public class ValidationException extends RuntimeException {
  private final Map<String, String> violation;
}
