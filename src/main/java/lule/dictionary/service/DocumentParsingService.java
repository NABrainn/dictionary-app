package lule.dictionary.service;

import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentParsingService {

    private final StringParsingService stringParsingService;

    public Document fetchContent(String url) throws IOException {
        return Jsoup.connect(url).get();
    }

    public List<String> parse(String content) {
        String wordsNormalized = stringParsingService.normalizeSpaces(content);
        return stringParsingService.toWhitespaceSplit(wordsNormalized);
    }
}
