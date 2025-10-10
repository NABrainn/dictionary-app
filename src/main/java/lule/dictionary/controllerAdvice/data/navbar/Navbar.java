package lule.dictionary.controllerAdvice.data.navbar;

import lule.dictionary.controllerAdvice.data.NavbarLocalizationKey;
import lule.dictionary.controllerAdvice.data.user.User;

import java.util.List;
import java.util.Map;

public sealed interface Navbar permits NavbarWithOpenPanel, NavbarWithClosedPanel {
    Map<NavbarLocalizationKey, String> localization();
    User user();
    List<LanguageOption> languageOptions();
}
