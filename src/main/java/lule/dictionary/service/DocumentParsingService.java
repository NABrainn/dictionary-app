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

    public Document fetchContent(String url) throws IOException {
        return Jsoup.connect(url).get();
    }

    public List<String> parse(String content) {
        String wordsNormalized = content.trim().replaceAll("\\s{2,}", " ");
        return List.of(wordsNormalized.split(" "));
    }
}
