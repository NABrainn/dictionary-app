package lule.dictionary.service.jsoup;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class JsoupService {

    public String importDocumentContent(String url) {
        return formatDocumentContent(importDocument(url));
    }

    public Document importDocument(String url) {
        try {
            return connect(url).get();
        } catch (IOException e) {
            throw new RuntimeException(e);
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
    private String formatDocumentContent(Document document) {
        return Arrays.stream(document.wholeText().split("\s+"))
                .map(word ->
                        switch (getEndlineCount(word)) {
                            case 0 -> word;
                            case 1 -> word.replaceAll("\n", " ");
                            default -> word.replaceAll("\n+", produceEndlines());
                        })
                .filter(word -> !word.isBlank() && !word.matches("\n+"))
                .reduce((s1, s2) ->s1 + " " + s2)
                .map(String::trim)
                .orElse("");
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
