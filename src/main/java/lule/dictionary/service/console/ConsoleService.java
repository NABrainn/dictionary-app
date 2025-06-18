package lule.dictionary.service.console;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.entity.application.interfaces.userProfile.base.UserProfile;
import lule.dictionary.exception.ServiceException;
import lule.dictionary.service.userProfile.UserProfileService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsoleService {


    private final UserProfileService userProfileService;

    private UserProfile userProfile;

    public void initialize() {
        try {
            this.userProfile = userProfile;

        } catch (ServiceException e) {
            log.info("User already exists");
        }

    }

//    public void openMainMenu() {
//    }
}
