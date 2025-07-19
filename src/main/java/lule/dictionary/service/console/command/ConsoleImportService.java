//package lule.dictionary.service.console.command;
//
//import lombok.RequiredArgsConstructor;
//import lule.dictionary.dto.application.interfaces.imports.base.Import;
//import lule.dictionary.dto.application.interfaces.translation.Translation;
//import lule.dictionary.dto.application.interfaces.userProfile.base.UserProfile;
//import lule.dictionary.enumeration.Familiarity;
//import lule.dictionary.service.language.Language;
//import lule.dictionary.factory.dto.ImportFactory;
//import lule.dictionary.factory.dto.TranslationFactory;
//import lule.dictionary.factory.dto.UserProfileFactory;
//import lule.dictionary.functionalInterface.Printer;
//import lule.dictionary.service.DocumentParser;
//import lule.dictionary.service.ImportService;
//import lule.dictionary.service.TranslationService;
//import org.springframework.stereotype.Service;
//
//import java.util.Map;
//import java.util.Scanner;
//
//@Service
//@RequiredArgsConstructor
//public class ConsoleImportService implements ConsoleCommand, Printer {
//
//    private final Scanner scanner = new Scanner(System.in);
//    private final ImportService importService;
//    private final DocumentParser parser;
//    private final TranslationService translationService;
//    private UserProfile userProfile;
//
//    public Map<String, String> takeUserInput() {
//        System.out.print("Title: ");
//        String title = scanner.nextLine();
//        System.out.print("Content: ");
//        String pageContent.jte = scanner.nextLine();
//        System.out.print("Url: ");
//        String url = scanner.nextLine();
//        if(url.isEmpty()) url = "";
//        return Map.of(
//                "title", title,
//                "pageContent.jte", pageContent.jte,
//                "url", url
//        );
//    }
//
//    public int saveImport(Import imported) {
//        Import addedImport = ImportFactory.createImport(
//                ImportFactory.createImportDetails(
//                        imported.importDetails().title(),
//                        imported.importDetails().pageContent.jte(),
//                        imported.importDetails().url()
//                ),
//                UserProfileFactory.createUserProfile(
//                        imported.userProfile().userProfileCredentials(),
//                        imported.userProfile().userProfileSettings()
//                )
//        );
//        userProfile = addedImport.userProfile();
//        return importService.add(addedImport);
//    }
//
//    public void processContent(String pageContent.jte, Language sourceLanguage, Language targetLanguage, String login, int importId) {
//        for (String targetWord : parser.wordsAsList(pageContent.jte)) {
//            System.out.println("Word: " + targetWord);
//            System.out.print("Meaning: ");
//            String sourceWord = scanner.nextLine();
//            System.out.println(sourceWord);
//            System.out.println("""
//                    Unknown: [0]
//                    Recognized: [1]
//                    Familiar: [2]
//                    Known: [3]
//                    Ignored: [4]
//                    """);
//            saveTranslation(TranslationFactory.createTranslation(
//                    TranslationFactory.createTranslationDetails(
//                            sourceWord,
//                            targetWord,
//                            Familiarity.UNKNOWN
//                    ),
//                    userProfile
//            ),  importId);
//        }
//    }
//    private void saveTranslation(Translation translation, int importId) {
//        while(true) {
//            Map<String, Familiarity> cases = Map.of(
//                    "0", Familiarity.UNKNOWN,
//                    "1", Familiarity.RECOGNIZED,
//                    "2", Familiarity.FAMILIAR,
//                    "3", Familiarity.KNOWN,
//                    "4", Familiarity.IGNORED
//            );
//            String input = scanner.nextLine();
//            for(var choice : cases.entrySet()) {
//                String key = choice.getKey();
//                if(key.equals(input)) {
//                    translationService.add(translation, importId);
//                    return;
//                }
//            }
//        }
//    }
//
//    @Override
//    public void print(String text) {
//        System.out.println(text);
//    }
//
//    @Override
//    public void execute() {
//
//    }
//}
