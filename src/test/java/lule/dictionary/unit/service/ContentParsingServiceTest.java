package lule.dictionary.unit.service;

import lule.dictionary.service.ContentParsingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.web.client.RestClient;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ContentParsingServiceTest {

    @Mock
    private ContentParsingService documentParser;

    @Mock
    private RestClient restClient;

    @BeforeEach
    void setup() {
        this.documentParser = new ContentParsingService(restClient);
    }

    @Test
    void shouldRemoveSpecialCharsAndDigits() {
        String actual1 = documentParser.removeNonLetters("XDDDDDDDDDDDDDDDDd. XDDDDDDDDDDDDDDDD.");
        assertEquals("XDDDDDDDDDDDDDDDDd  XDDDDDDDDDDDDDDDD ", actual1);
    }
    @Test
    void shouldRemoveIndentations() {
        String before = documentParser.removeNonLetters("""
                		<dependency>
                			<groupId>org.springframework.boot</groupId>
                			<artifactId>spring-boot-starter-data-jpa</artifactId>
                		</dependency>
                		<dependency>
                			<groupId>org.springframework.boot</groupId>
                			<artifactId>spring-boot-starter-web</artifactId>
                		</dependency>
                		<dependency>
                			<groupId>org
                """);
        String actual2 = documentParser.normalizeSpaces(before);
        assertEquals("dependency groupId org springframework boot groupId artifactId spring boot starter data jpa artifactId dependency dependency groupId org springframework boot groupId artifactId spring boot starter web artifactId dependency dependency groupId org", actual2);
    }

}
