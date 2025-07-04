package lule.dictionary.configuration.security.filter.timezone;

public class TimeZoneOffsetContext {
    private static final ThreadLocal<String> timeZoneOffsetHolder = new ThreadLocal<>();

    public static String get() {
        return timeZoneOffsetHolder.get();
    }

    public static void set(String timeZoneOffset) {
        timeZoneOffsetHolder.set(timeZoneOffset);
    }

    public static void reset() {
        timeZoneOffsetHolder.remove();
    }
}
