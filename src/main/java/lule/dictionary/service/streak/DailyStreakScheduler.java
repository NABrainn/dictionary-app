package lule.dictionary.service.streak;

import lombok.RequiredArgsConstructor;
import lule.dictionary.service.userProfile.UserProfileService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DailyStreakScheduler {

    private final UserProfileService userProfileService;

    @Scheduled(cron = "0 0 * * * *")
    public void resetStreaksIfMidnight() {
        userProfileService.resetStreaksIfMidnight();
    }
}
