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
        try {
            String validatedUrl = (!url.startsWith("https://") && !url.startsWith("http://")) ?
                    "https://".concat(url) :
                    url;
            Connection connection = Jsoup.connect(validatedUrl);
            String[] documentAsArray = connection.get().wholeText().split(" ");
            Pattern singleNewLinePattern = Pattern.compile("\n");
            Pattern multiNewLinePattern = Pattern.compile("\n+");
            return Arrays.stream(documentAsArray)
                    .map(word ->
                        switch ((int) word.chars()
                                .filter(ch -> ch == '\n')
                                .count()) {
                            case 0 -> word;
                            case 1 -> singleNewLinePattern.matcher(word)
                                    .replaceAll(" ");
                            default -> multiNewLinePattern.matcher(word)
                                    .replaceAll(Stream.generate(() -> '\n')
                                            .limit(30)
                                            .map(String::valueOf)
                                            .collect(Collectors.joining()));
                        })
                    .filter(word -> !word.isBlank() && !word.matches("\n+"))
                    .reduce((s1, s2) ->s1 + " " + s2)
                    .map(String::trim)
                    .orElse("");
        } catch (IOException e) {
            throw new InvalidUriException("Invalid or empty URL");
        }
    }
}
