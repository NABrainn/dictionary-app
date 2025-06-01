package lule.dictionary.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DocumentParser {

    private final RestClient restClient;

    public Optional<String> fetchImport(String url) {
        String fetched =  restClient
                .get()
                .uri(url)
                .retrieve()
                .body(String.class);
        return Optional.ofNullable(fetched);
    }

    public List<String> parse(String content) {
        Optional<String> words = convertToWords(content);
        if(words.isPresent()) {
            String wordsWithoutSpecials = removeNonLetters(words.get());
            String wordsNormalized = normalizeSpaces(wordsWithoutSpecials);
            return convertToList(wordsNormalized);
        }
        return List.of();
    }
    private List<String> convertToList(String content) {
        return Arrays.stream(content.split(" ")).toList();
    }
    private Optional<String> convertToWords(String content) {
        if(content != null) {
            String fetchedOnlyWords = removeNonLetters(content);
            return Optional.of(normalizeSpaces(fetchedOnlyWords));
        }
        return Optional.empty();
    }

    public String removeNonLetters(String content) {
        return content.replaceAll("[%&/^!<>@#$'\"*;`:=\\-_+.,\\t\\n(){}\\[\\]?\\\\1234567890]", " ");
    }

    public String normalizeSpaces(String content) {
        return content.trim().replaceAll("\\s{2,}", " ");
    }
}
