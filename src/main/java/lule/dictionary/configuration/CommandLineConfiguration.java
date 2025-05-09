package lule.dictionary.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.exception.ServiceException;
import lule.dictionary.factory.UserProfileFactory;
import lule.dictionary.service.DialogManager;
import lule.dictionary.service.UserProfileService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class CommandLineConfiguration {

    private final UserProfileService userProfileService;

    @Bean
    public CommandLineRunner runner() {
        return  args -> {
            DialogManager.start(DialogManager.createDialog(
                    this::initializeUserProfile,
                    DialogManager.createOption(
                            "Import a text", "i", DialogManager.executeDialog(DialogManager.createDialog(
                                    () -> System.out.println("running Import"),
                                    DialogManager.createOption("Confirm", "y", DialogManager.openPrevious()),
                                    DialogManager.createOption("Cancel", "n", DialogManager.openPrevious()),
                                    DialogManager.createOption("Retry", "r", DialogManager.reopenCurrent())
                            ))
                    ),
                    DialogManager.createOption(
                            "Review translations", "t", DialogManager.executeDialog(DialogManager.createDialog(
                                    () -> System.out.println("running Translations"),
                                    DialogManager.createOption("Cancel", "n", DialogManager.openPrevious())
                            ))
                    ),
                    DialogManager.createOption(
                            "Open profile settings", "p", DialogManager.executeDialog(DialogManager.createDialog(
                                    () -> System.out.println("running ProfileSettings"),
                                    DialogManager.createOption("Confirm", "y", DialogManager.openPrevious()),
                                    DialogManager.createOption("Cancel", "n", DialogManager.openPrevious()),
                                    DialogManager.createOption("Retry", "r", DialogManager.reopenCurrent())
                            ))
                    ),
                    DialogManager.createOption(
                            "Exit", "x", DialogManager.executeDialog(DialogManager.createDialog(
                                    DialogManager.terminate()
                            ))
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
