//package lule.dictionary.unit.service;
//
//import lule.dictionary.service.streak.DailyStreakScheduler;
//import lule.dictionary.service.userProfile.UserProfileService;
//import org.junit.jupiter.api.Test;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Service;
//
//import java.util.TimeZone;
//
//@Service
//public class DailyStreakSchedulerTest {
//
//    private final DailyStreakScheduler dailyStreakScheduler;
//
//    public DailyStreakSchedulerTest() {
//        this.dailyStreakScheduler = new DailyStreakScheduler(new UserProfileService(new JdbcTemplate()));
//    }
//
//    @Test
//    public void shouldPrintTimeConversions() {
//        System.out.println(TimeZone.getTimeZone("CEST"));
//
//    }
//}
