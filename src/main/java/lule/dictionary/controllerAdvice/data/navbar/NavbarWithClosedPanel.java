package lule.dictionary.controllerAdvice.data.navbar;

import lombok.NonNull;
import lule.dictionary.controllerAdvice.data.NavbarLocalizationKey;
import lule.dictionary.controllerAdvice.data.user.User;

import java.util.List;
import java.util.Map;

public record NavbarWithClosedPanel(@NonNull Map<NavbarLocalizationKey, String> localization,
                                    @NonNull User user,
                                    @NonNull List<LanguageOption> languageOptions) implements Navbar {
    public static NavbarWithClosedPanel of(Map<NavbarLocalizationKey, String> localization,
                                           User user,
                                           List<LanguageOption> languageOptions) {
        return new NavbarWithClosedPanel(localization, user, languageOptions);
    }
}