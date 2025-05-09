package lule.dictionary.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.exception.ServiceException;
import lule.dictionary.factory.Dialogs;
import lule.dictionary.factory.DialogOptions;
import lule.dictionary.factory.UserProfileFactory;
import lule.dictionary.service.DialogManager;
import lule.dictionary.service.ImportService;
import lule.dictionary.service.UserProfileService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class CommandLineRunner {

    private final DialogManager cli;
    private final UserProfileService userProfileService;
    private final ImportService importService;

    @Bean
    public org.springframework.boot.CommandLineRunner runner() {
        return  args -> {
            initializeUserProfile();
            cli.executeDialog(Dialogs.create(
                    () -> System.out.println("running Main Menu"),
                    List.of(
                            DialogOptions.create(
                                    "Import a text", "i", cli.executeDialog(Dialogs.create(
                                            () -> System.out.println("running Import"), List.of(
                                                    DialogOptions.create("Confirm", "y", cli.printTranslations()),
                                                    DialogOptions.create("Cancel", "n", cli.openPrevious()),
                                                    DialogOptions.create("Retry", "r", cli.retryDialog())
                                            )
                                    ))
                            ),
                            DialogOptions.create(
                                    "Review translations", "t", cli.executeDialog(Dialogs.create(
                                            () -> System.out.println("running Translations"),
                                            List.of(
                                                    DialogOptions.create("Cancel", "n", cli.openPrevious())
                                            )
                                    ))
                            ),
                            DialogOptions.create(
                                    "Open profile settings", "p", cli.executeDialog(Dialogs.create(
                                            () -> System.out.println("running ProfileSettings"),
                                            List.of(
                                                    DialogOptions.create("Confirm", "y", cli.openPrevious()),
                                                    DialogOptions.create("Cancel", "n", cli.openPrevious()),
                                                    DialogOptions.create("Retry", "r", cli.retryDialog())
                                            )
                                    ))
                            ),
                            DialogOptions.create(
                                    "Exit", "x", cli.terminate(Dialogs.create(
                                            () -> System.out.println("running ProfileSettings"),
                                            List.of(

                                            )
                                    ))
                            )
                    )
            ));
        };
    }

    private void initializeUserProfile() {
        System.out.println("------------------------------");
        try {
            userProfileService.add(UserProfileFactory.create(
                    "cliuser",
                    "cliuser@cliuser.com",
                    "password"
            ));
        } catch (ServiceException e) {
            userProfileService.findByUsername("cliuser");
        }
        System.out.println("------------------------------");
    }
}
