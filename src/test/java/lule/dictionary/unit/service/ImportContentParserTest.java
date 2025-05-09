//package lule.dictionary.unit.service;
//
//import lule.dictionary.service.ImportFetchingService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//public class ImportContentParserTest {
//
//    @Mock
//    private ImportFetchingService importContentParser;
//
//    @BeforeEach
//    void setup() {
//        this.importContentParser = new ImportFetchingService();
//    }
//
//    @Test
//    void shouldRemoveSpecialCharsAndDigits() {
//        String actual1 = importContentParser.removeNonLetters("XDDDDDDDDDDDDDDDDd. XDDDDDDDDDDDDDDDD.");
//        assertEquals("XDDDDDDDDDDDDDDDDd  XDDDDDDDDDDDDDDDD ", actual1);
//    }
//    @Test
//    void shouldRemoveIndentations() {
//        String before = importContentParser.removeNonLetters("""
//                		<dependency>
//                			<groupId>org.springframework.boot</groupId>
//                			<artifactId>spring-boot-starter-data-jpa</artifactId>
//                		</dependency>
//                		<dependency>
//                			<groupId>org.springframework.boot</groupId>
//                			<artifactId>spring-boot-starter-web</artifactId>
//                		</dependency>
//                		<dependency>
//                			<groupId>org
//                """);
//        String actual2 = importContentParser.normalizeSpaces(before);
//        assertEquals("dependency groupId org springframework boot groupId artifactId spring boot starter data jpa artifactId dependency dependency groupId org springframework boot groupId artifactId spring boot starter web artifactId dependency dependency groupId org", actual2);
//    }
//
//}
