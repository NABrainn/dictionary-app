package lule.dictionary.userProfiles.service;

import lombok.RequiredArgsConstructor;
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
