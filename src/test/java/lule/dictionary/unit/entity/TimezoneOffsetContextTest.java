package lule.dictionary.unit.entity;

import lombok.extern.slf4j.Slf4j;
import lule.dictionary.configuration.security.filter.timezone.TimeZoneOffsetContext;
import org.junit.jupiter.api.Test;

@Slf4j
public class TimezoneOffsetContextTest {

    @Test
    void shouldshould() {
        log.info(TimeZoneOffsetContext.get());
    }
}
