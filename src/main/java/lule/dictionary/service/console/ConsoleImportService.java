package lule.dictionary.service.console;

import lombok.RequiredArgsConstructor;
import lule.dictionary.enumeration.Familiarity;
import lule.dictionary.enumeration.Language;
import lule.dictionary.factory.ImportFactory;
import lule.dictionary.factory.TranslationFactory;
import lule.dictionary.functionalInterface.Printer;
import lule.dictionary.service.DocumentParser;
import lule.dictionary.service.ImportService;
import lule.dictionary.service.TranslationService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Scanner;

@Service
@RequiredArgsConstructor
public class ConsoleImportService implements Printer {

    private final Scanner scanner = new Scanner(System.in);
    private final ImportService importService;
    private final DocumentParser parser;
    private final TranslationService translationService;

    public Map<String, String> takeUserInput() {
        System.out.print("Title: ");
        String title = scanner.nextLine();
        System.out.print("Content: ");
        String content = scanner.nextLine();
        System.out.print("Url: ");
        String url = scanner.nextLine();
        if(url.isEmpty()) url = "";
        return Map.of(
                "title", title,
                "content", content,
                "url", url
        );
    }

    public int saveImport(String title, String content, String url, Language sourceLanguage, Language targetLanguage, String username) {
        return importService.add(ImportFactory.create(
                title,
                content,
                url,
                sourceLanguage,
                targetLanguage,
                username
        ));
    }

    public void processContent(String content, Language sourceLanguage, Language targetLanguage, String username, int importId) {
        for (String targetWord : parser.wordsAsList(content)) {
            System.out.println("Word: " + targetWord);
            System.out.print("Meaning: ");
            String sourceWord = scanner.nextLine();
            System.out.println(sourceWord);
            System.out.println("""
                    Unknown: [0]
                    Recognized: [1]
                    Familiar: [2]
                    Known: [3]
                    Ignored: [4]
                    """);
            saveTranslation(targetWord, sourceWord, sourceLanguage, targetLanguage, username,  importId);
        }
    }
    private void saveTranslation(String targetWord, String sourceWord, Language sourceLanguage, Language targetLanguage, String username, int importId) {
        while(true) {
            Map<String, Familiarity> cases = Map.of(
                    "0", Familiarity.UNKNOWN,
                    "1", Familiarity.RECOGNIZED,
                    "2", Familiarity.FAMILIAR,
                    "3", Familiarity.KNOWN,
                    "4", Familiarity.IGNORED
            );
            String input = scanner.nextLine();
            for(var choice : cases.entrySet()) {
                String key = choice.getKey();
                if(key.equals(input)) {
                    translationService.add(
                            TranslationFactory.create(
                                    0,
                                    sourceWord,
                                    targetWord,
                                    sourceLanguage,
                                    targetLanguage,
                                    username,
                                    cases.get(key)
                            ),
                            importId
                    );
                    return;
                }
            }
        }
    }

    @Override
    public void print(String text) {
        System.out.println(text);
    }
}
