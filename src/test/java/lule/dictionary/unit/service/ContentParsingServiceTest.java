package lule.dictionary.unit.service;

import lombok.extern.slf4j.Slf4j;
import lule.dictionary.service.DocumentParsingService;
import lule.dictionary.service.StringParsingService;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.io.IOException;
import java.util.List;

@Slf4j
public class ContentParsingServiceTest {

    @Mock
    private DocumentParsingService documentParser;

    @Mock
    private StringParsingService stringParsingService = new StringParsingService();

    @BeforeEach
    void setup() {
        this.documentParser = new DocumentParsingService(stringParsingService);
    }
    @Test
    void shouldFetchContent() {
        try {
            Document content = documentParser.fetchContent("https://yr.no/nb");
            Elements tags = content.select("a, span, div");
            log.info(tags.text());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void shouldFetchAndParseContent() {
        try {
            Document content = documentParser.fetchContent("https://yr.no/nb");
            Elements tags = content.select("a, span, div");
            List<String> parsed = documentParser.parse(tags.text());
            log.info("{}", parsed);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
