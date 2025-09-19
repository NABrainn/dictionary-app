package lule.dictionary.documents.service;

import lombok.RequiredArgsConstructor;
import lule.dictionary.documents.data.entity.Document;
import lule.dictionary.documents.data.request.SanitizeNumberOfPagesRequest;
import lule.dictionary.result.data.Err;
import lule.dictionary.result.data.Ok;
import lule.dictionary.result.data.Result;
import org.springframework.stereotype.Service;
import org.springframework.web.util.InvalidUrlException;

@Service
@RequiredArgsConstructor
public class DocumentSanitizer {
    public Result<?> validateNumberOfPages(SanitizeNumberOfPagesRequest request) {
        return request.page() <= 0 || request.page() > request.numberOfPages() ? Err.of(new InvalidUrlException("Invalid url parameter provided")) : Ok.empty();
    }
}
