package lule.dictionary.service;

import org.springframework.stereotype.Service;

@Service
public class ImportContentParser {

    public String removeNonLetters(String content) {
        return content.replaceAll("[%&/^!<>@#$'\"*;`:=\\-_+.,\\t\\n(){}\\[\\]?\\\\1234567890]", " ");
    }

    public String normalizeSpaces(String content) {
        content = content.trim();
        content = content.replaceAll("\\s{2,}", " ");
        return content;
    }
}
