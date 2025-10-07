package lule.dictionary.jsoup.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lule.dictionary.jsoup.data.InvalidUriException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class JsoupService {

    public Document fetchDocument(@NonNull String url) throws InvalidUriException {
        try {
            String validatedUrl = (!url.startsWith("https://") && !url.startsWith("http://")) ?
                    "https://".concat(url) :
                    url;
            Connection connection = Jsoup.connect(validatedUrl);
            return connection.get();
        } catch (IOException e) {
            throw new InvalidUriException("Invalid or empty URL");
        }
    }
}
