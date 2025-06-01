package lule.dictionary.service.console;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.dto.application.interfaces.userProfile.UserProfile;
import lule.dictionary.exception.ServiceException;
import lule.dictionary.functionalInterface.Printer;
import lule.dictionary.service.application.dto.UserProfileService;
//import lule.dictionary.service.console.command.ConsoleImportService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsoleService implements Printer {


    private final UserProfileService userProfileService;

    private UserProfile userProfile;

    public void initialize() {
        try {
            this.userProfile = userProfile;

        } catch (ServiceException e) {
            log.info("User already exists");
        }

    }

    public void openMainMenu() {
        print("""
                ------------------------------
                -----------MAIN MENU----------
                ------------------------------
                """
        );
    }

    @Override
    public void print(String text) {
        System.out.println(text);
    }
}
