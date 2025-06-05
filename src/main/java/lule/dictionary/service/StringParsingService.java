package lule.dictionary.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StringParsingService {

    public List<String> toWhitespaceSplit(String input) {
        return List.of(input.split(" "));
    }

    public String normalizeSpaces(String content) {
        return content.trim().replaceAll("\\s{2,}", " ");
    }

    public String removeNonLetters(String content) {
        return content.replaceAll("[^\\p{L}\\p{N}]", "");
    }
}
