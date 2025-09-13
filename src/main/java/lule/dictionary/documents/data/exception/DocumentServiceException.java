package lule.dictionary.documents.data.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lule.dictionary.documents.data.attribute.DocumentFormAttribute;

import java.util.Map;

@Getter
@RequiredArgsConstructor
public class DocumentServiceException extends RuntimeException {
    private final DocumentFormAttribute attribute;
    private final Map<String, String> violation;
}
