//package lule.dictionary.service.console.command;
//
//import lombok.RequiredArgsConstructor;
//import lule.dictionary.dto.application.implementation.translation.DictionaryTranslation;
//import lule.dictionary.dto.application.interfaces.translation.Translation;
//import lule.dictionary.enumeration.Familiarity;
//import lule.dictionary.functionalInterface.Printer;
//import lule.dictionary.service.TranslationService;
//import org.springframework.stereotype.Service;
//
//import java.util.Map;
//import java.util.Scanner;
//
//@Service
//@RequiredArgsConstructor
//public class ConsoleTranslationService implements Printer {
//
//    private final Scanner scanner = new Scanner(System.in);
//    private final TranslationService translationService;
//
//    public void fetchTranslations(String username) {
//        for(Translation translation : translationService.findAllByOwner(username)) {
//            System.out.println("Word: " + translation.translationDetails().targetWord());
//            System.out.print("Meaning: ");
//            System.out.println(translation.translationDetails().sourceWord());
//            System.out.println("""
//                    Unknown: [0]
//                    Recognized: [1]
//                    Familiar: [2]
//                    Known: [3]
//                    Ignored: [4]
//                    """);
//            updateFamiliarity(translation);
//        }
//    }
//
//    private void updateFamiliarity(Translation translation) {
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
//                    translationService.updateFamiliarity(translation, choice.getValue());
//                    return;
//                }
//            }
//        }
//    }
//
//    @Override
//    public void print(String text) {
//        System.out.println(text);
//        System.out.println("""
//                ------------------------------
//                ---------TRANSLATIONS---------
//                ------------------------------
//                """);
//    }
//}
