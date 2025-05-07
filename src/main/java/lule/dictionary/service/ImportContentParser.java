package lule.dictionary.service;

import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class ImportContentParser {

    static Pattern pattern;

    public String replaceAllMatching(String content) {
        pattern = Pattern.compile("[%&/^!<>@#$'\"*;`:=\\-_+.,\\t\\n(){}\\[\\]?\\\\1234567890]");
        return content.replaceAll(pattern.toString(), " ");
    }

    public String normalizeSpaces(String content) {
        content = content.trim();
        pattern = Pattern.compile("\\s{2,}");
        content = content.replaceAll(pattern.toString(), " ");
        return content;
    }
}
