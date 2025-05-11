package lule.dictionary.service.console;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.enumeration.Language;
import lule.dictionary.exception.ServiceException;
import lule.dictionary.factory.UserProfileFactory;
import lule.dictionary.functionalInterface.Printer;
import lule.dictionary.service.UserProfileService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsoleService implements Printer {


    private final UserProfileService userProfileService;
    private final ConsoleTranslationService consoleTranslationService;
    private final ConsoleImportService consoleImportService;
    private final ConsoleSettingsService consoleSettingsService;

    private String username;
    private Language sourceLanguage;
    private Language targetLanguage;

    public void initialize() {
        try {
            userProfileService.add(UserProfileFactory.create(
                    "cliuser",
                    "cliuser@cliuser.com",
                    "password"
            ));
        } catch (ServiceException e) {
            log.info("User already exists");
        }
        username = "cliuser";
        sourceLanguage = Language.EN;
        targetLanguage = Language.NO;
    }

    public void openMainMenu() {
        print("""
                ------------------------------
                -----------MAIN MENU----------
                ------------------------------
                """
        );
    }

    public void importText() {
        consoleImportService.print("""
                ------------------------------
                ----------IMPORT TEXT---------
                ------------------------------
                """);
        Map<String, String> inputs = consoleImportService.takeUserInput();
        int importId = consoleImportService.saveImport(
                inputs.get("title"),
                inputs.get("content"),
                inputs.get("url"),
                sourceLanguage,
                targetLanguage,
                username
        );
        consoleImportService.print("Import added, opening...");
        consoleImportService.processContent(
                inputs.get("content"),
                sourceLanguage,
                targetLanguage,
                username,
                importId
        );
        consoleImportService.print("Book content covered");
    }

    public void reviewTranslations() {
        consoleTranslationService.print("""
                ------------------------------
                ---------TRANSLATIONS---------
                ------------------------------
                """);
        consoleTranslationService.print("Fetching all translations...");
        consoleTranslationService.fetchTranslations(username);
        consoleTranslationService.print("All words have been reviewed");
    }

    public void profileSettings() {
        consoleSettingsService.print("""
                ------------------------------
                -----------SETTINGS-----------
                ------------------------------
                """);
    }

    @Override
    public void print(String text) {
        System.out.println(text);
    }
}
