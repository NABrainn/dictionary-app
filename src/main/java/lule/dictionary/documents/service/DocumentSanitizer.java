package lule.dictionary.documents.service;

import lombok.RequiredArgsConstructor;
import lule.dictionary.documents.data.request.SanitizeNumberOfPagesRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.util.InvalidUrlException;

@Service
@RequiredArgsConstructor
public class DocumentSanitizer {
    public void validateNumberOfPages(SanitizeNumberOfPagesRequest request) {
        if(request.page() <= 0 || request.page() > request.numberOfPages()) {
            System.out.println(request.page());
            System.out.println(request.numberOfPages());
            throw new InvalidUrlException("Invalid url parameter provided");
        }
    }
}
