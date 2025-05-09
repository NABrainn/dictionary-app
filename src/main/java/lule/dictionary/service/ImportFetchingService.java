package lule.dictionary.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImportFetchingService {

    private final RestClient restClient;

    public Optional<String> fetchImport(String url) {
        String fetched =  restClient
                .get()
                .uri(url)
                .retrieve()
                .body(String.class);
        return Optional.ofNullable(fetched);
    }
    public Optional<String> convertToWords(String content) {
        if(content != null) {
            String fetchedOnlyWords = removeNonLetters(content);
            return Optional.of(normalizeSpaces(fetchedOnlyWords));
        }
        return Optional.empty();
    }

    private String removeNonLetters(String content) {
        return content.replaceAll("[%&/^!<>@#$'\"*;`:=\\-_+.,\\t\\n(){}\\[\\]?\\\\1234567890]", " ");
    }

    private String normalizeSpaces(String content) {
        content = content.trim();
        content = content.replaceAll("\\s{2,}", " ");
        return content;
    }
}
