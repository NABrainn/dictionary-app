package lule.dictionary.service.console;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.dto.database.interfaces.userProfile.base.UserProfile;
import lule.dictionary.service.userProfile.UserProfileService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsoleService {


    private final UserProfileService userProfileService;

    private UserProfile userProfile;

    public void initialize() {

    }

//    public void openMainMenu() {
//    }
}
