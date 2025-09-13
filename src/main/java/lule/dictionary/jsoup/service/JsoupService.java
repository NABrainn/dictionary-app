package lule.dictionary.jsoup.service;

import lule.dictionary.jsoup.service.exception.InvalidUriException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class JsoupService {

    public String importDocumentContent(String url) throws InvalidUriException {
        Pattern singleNewLinePattern = Pattern.compile("\n");
        Pattern multiNewLinePattern = Pattern.compile("\n+");
        Document document = importDocument(url);
        var patternMap = Map.of("singleNewLine", singleNewLinePattern, "multiNewLine", multiNewLinePattern);
        return Arrays.stream(document.wholeText().split(" "))
                .map(word ->
                        switch (getEndlineCount(word)) {
                            case 0 -> word;
                            case 1 -> patternMap.get("singleNewLine").matcher(word).replaceAll(" ");
                            default -> patternMap.get("multiNewLine").matcher(word).replaceAll(produceEndlines());
                        })
                .filter(word -> !word.isBlank() && !word.matches("\n+"))
                .reduce((s1, s2) ->s1 + " " + s2)
                .map(String::trim)
                .orElse("");
    }

    public Document importDocument(String url) {
        try {
            return connect(url).get();
        } catch (IOException e) {
            throw new InvalidUriException("Invalid or empty URL");
        }
    }

    private Connection connect(String url) {
        return Jsoup.connect(prependHttpFormat(url));
    }

    private String prependHttpFormat(String url) {
        if(!url.startsWith("https://") && !url.startsWith("http://"))
            return "https://".concat(url);
        return url;
    }

    private int getEndlineCount(String word) {
        return (int) word.chars()
                .filter(ch -> ch == '\n')
                .count();
    }

    private String produceEndlines() {
        return Stream.generate(() -> '\n')
                .limit(30)
                .map(String::valueOf)
                .collect(Collectors.joining());
    }

}
