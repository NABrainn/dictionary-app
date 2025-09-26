package lule.dictionary.jsoup.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lule.dictionary.jsoup.data.Token;
import lule.dictionary.jsoup.service.exception.InvalidUriException;
import lule.dictionary.stringUtil.service.PatternService;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JsoupService {

    private final PatternService patternService;

    public String importDocumentContent(@NonNull String url) throws InvalidUriException {
        try {
            String validatedUrl = (!url.startsWith("https://") && !url.startsWith("http://")) ?
                    "https://".concat(url) :
                    url;
            Connection connection = Jsoup.connect(validatedUrl);
            Document document = connection.get();
            String[] documentAsArray = document.wholeText().split("(?<=\\n)(?=\\w)");
            return Arrays.stream(documentAsArray)
                    .map(tokenBlob -> Token.of(tokenBlob, (int) tokenBlob.chars().filter(ch -> ch == '\n').count()))
                    .map(token -> switch (token.newlineCount()) {
                        case 0 -> token;
                        case 1 -> token.withContent(patternService.replaceNewline(token.content(), " "));
                        default -> token.withContent(patternService.replaceAllNewlines(token.content(), '\n', 30));
                    })
                    .map(Token::content)
                    .filter(Predicate.not(String::isBlank))
                    .collect(Collectors.joining());
        } catch (IOException e) {
            throw new InvalidUriException("Invalid or empty URL");
        }
    }
}
