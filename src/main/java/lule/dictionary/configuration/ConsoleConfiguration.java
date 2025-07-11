package lule.dictionary.configuration;

import lombok.RequiredArgsConstructor;
import lule.dictionary.service.console.ConsoleService;
import lule.dictionary.util.ConsoleDialogManager;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ConsoleConfiguration {

    private final ConsoleService commandLineService;

    @Bean
    public CommandLineRunner runner() {
        return  args -> {
            boolean runnable = hasArg("--console-application", args);
            if (runnable) {
                commandLineService.initialize();
                ConsoleDialogManager.start(ConsoleDialogManager.createDialog(
                        () -> System.out.println("do nothing"),
                        ConsoleDialogManager.createOption(
                                "Import a text", "i", ConsoleDialogManager.executeDialog(ConsoleDialogManager.createDialog(
                                        () -> System.out.println("placeholder"),
                                        ConsoleDialogManager.createOption("Main menu", "b", ConsoleDialogManager.openPrevious()),
                                        ConsoleDialogManager.createOption("Import", "i", ConsoleDialogManager.reopenCurrent())
                                ))
                        ),
                        ConsoleDialogManager.createOption(
                                "Review translations", "t", ConsoleDialogManager.executeDialog(ConsoleDialogManager.createDialog(
                                        () -> System.out.println("placeholder"),
                                        ConsoleDialogManager.createOption("Main menu", "b", ConsoleDialogManager.openPrevious()),
                                        ConsoleDialogManager.createOption("Review", "r", ConsoleDialogManager.reopenCurrent())
                                ))
                        ),
                        ConsoleDialogManager.createOption(
                                "Open profile settings", "p", ConsoleDialogManager.executeDialog(ConsoleDialogManager.createDialog(
                                        () -> System.out.println("placeholder"),
                                        ConsoleDialogManager.createOption("Main menu", "b", ConsoleDialogManager.openPrevious()),
                                        ConsoleDialogManager.createOption("Settings", "s", ConsoleDialogManager.reopenCurrent())
                                ))
                        ),
                        ConsoleDialogManager.createOption(
                                "Exit", "x", ConsoleDialogManager.executeDialog(ConsoleDialogManager.createDialog(
                                        ConsoleDialogManager.terminate()
                                ))
                        )
                ));
            }
        };
    }

    private boolean hasArg(String option, String... args) {
        for (String arg : args) {
            if (arg.startsWith(option)) {
                return true;
            }
        }
        return false;
    }
}
