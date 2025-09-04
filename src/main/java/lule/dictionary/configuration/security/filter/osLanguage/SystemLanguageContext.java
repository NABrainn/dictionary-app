package lule.dictionary.configuration.security.filter.osLanguage;

import lule.dictionary.language.service.Language;

public class SystemLanguageContext {
    private static final ThreadLocal<Language> systemLanguageHolder = new ThreadLocal<>();

    public static Language get() {
        return systemLanguageHolder.get();
    }

    public static void set(Language systemLanguage) {
        systemLanguageHolder.set(systemLanguage);
    }

    public static void reset() {
        systemLanguageHolder.remove();
    }
}
