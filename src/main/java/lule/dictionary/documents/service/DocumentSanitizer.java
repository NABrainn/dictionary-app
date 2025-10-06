package lule.dictionary.documents.service;

import lombok.RequiredArgsConstructor;
import lule.dictionary.documents.data.entity.Document;
import lule.dictionary.documents.data.request.DocumentPageDetails;
import lule.dictionary.result.data.Err;
import lule.dictionary.result.data.Ok;
import lule.dictionary.result.data.Result;
import org.springframework.stereotype.Service;
import org.springframework.web.util.InvalidUrlException;

@Service
@RequiredArgsConstructor
public class DocumentSanitizer {
    public Result<Document> validateRequestedPage(DocumentPageDetails details) {
        return details.page() <= 0 || details.page() > details.numberOfPages() ?
                Err.of(new InvalidUrlException("Invalid url parameter provided")) :
                Ok.of(details.document());
    }
}
