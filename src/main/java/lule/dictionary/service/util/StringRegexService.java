package lule.dictionary.service.util;

import org.springframework.stereotype.Service;

@Service
public class StringRegexService {

    public String removeNonLetters(String input) {
        return input.replaceAll("[^\\p{L}\\p{N}]", "").trim().toLowerCase();
    }
}
