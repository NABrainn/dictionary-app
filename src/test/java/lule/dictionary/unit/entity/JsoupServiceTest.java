package lule.dictionary.unit.entity;

import lombok.extern.slf4j.Slf4j;
import lule.dictionary.jsoup.service.JsoupService;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
public class JsoupServiceTest {

    private final JsoupService jsoupService;

    public JsoupServiceTest() {
        this.jsoupService = new JsoupService();
    }

    @Test
    void shouldImportDocument() throws IOException {
        Document document = jsoupService.importDocument("https://www.nrk.no/sport/audun-gronvold-er-dod-1.17494151");
        log.info("whole text, separated: {}", formatDocumentContent(document));
    }

    private String formatDocumentContent(Document document) {
        return Arrays.stream(document.wholeText().split(" "))
                .map(word ->
                        switch ((int) word.chars()
                                .filter(ch -> ch == '\n')
                                .count()) {
                        case 0 -> word;
                        case 1 -> word.replaceAll("\n", " ");
                        default -> word.replaceAll("\n+", "\n");
                    })
                .filter(word -> !word.isBlank())
                .reduce("", (s1, s2) -> s1 + " " + s2);
    }
}
