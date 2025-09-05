package lule.dictionary.shared;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    public static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern(
                    "dd/MM/uuuu HH:mm:ss"
            );

    public static ZoneOffset stringToZoneOffset(String timeZoneOffset) {
        if (timeZoneOffset != null) {
            int offsetMinutes = Integer.parseInt(timeZoneOffset) * -1;
            return ZoneOffset.ofTotalSeconds(offsetMinutes * 60);
        }
        return null;
    }

    public static String getFormattedDateTime(ZoneOffset zoneOffset, LocalDateTime dateTime) {
        if(zoneOffset != null) {
            OffsetDateTime serverOffsetDateTime = dateTime.atZone(
                    ZoneId.systemDefault()
            ).toOffsetDateTime();

            OffsetDateTime clientOffsetDateTime = serverOffsetDateTime
                    .withOffsetSameInstant(zoneOffset);

            return DATE_TIME_FORMATTER.format(clientOffsetDateTime);
        }
        return dateTime.format(DATE_TIME_FORMATTER);
    }

    public static long getSecondsUntilMidnight(ZoneOffset zoneOffset) {
        OffsetDateTime now = OffsetDateTime.now(ZoneId.systemDefault());
        OffsetDateTime clientTime = now.withOffsetSameInstant(zoneOffset);
        OffsetDateTime nextMidnight = clientTime
                .toLocalDate()
                .plusDays(1)
                .atStartOfDay()
                .atOffset(zoneOffset);

        return Duration.between(clientTime, nextMidnight).getSeconds();
    }
}
