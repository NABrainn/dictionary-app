package lule.dictionary.controller.auth;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.dto.application.LanguageData;
import lule.dictionary.dto.application.attribute.ProfilePanelAttribute;
import lule.dictionary.dto.database.interfaces.userProfile.CustomUserDetails;
import lule.dictionary.service.auth.dto.request.imp.LoginRequest;
import lule.dictionary.service.auth.AuthService;
import lule.dictionary.service.auth.dto.request.imp.SignupRequest;
import lule.dictionary.dto.application.result.ServiceResult;
import lule.dictionary.service.language.Language;
import lule.dictionary.service.language.LanguageHelper;
import lule.dictionary.service.localization.LocalizationService;
import lule.dictionary.service.translation.TranslationService;
import lule.dictionary.service.translation.dto.request.GetWordsLearnedCountRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthControllerImp implements AuthController {

    private final AuthService authService;
    private final LocalizationService localizationService;
    private final LanguageHelper languageHelper;
    private final TranslationService translationService;

    @GetMapping({"/login", "/login/"})
    public String loginPage(Model model,
                            RedirectAttributes redirectAttributes,
                            Authentication authentication,
                            HttpSession httpSession) {
        if(authentication != null) {
            CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
            redirectAttributes.addFlashAttribute("navbarLocalization", localizationService.navbarLocalization(principal.sourceLanguage()));
            redirectAttributes.addFlashAttribute("documentsLocalization", localizationService.documentListLocalization(principal.sourceLanguage()));
            redirectAttributes.addFlashAttribute("profilePanelAttribute", ProfilePanelAttribute.builder()
                    .languageDataList(languageHelper.getAllLanguageData())
                    .targetLanguage(LanguageData.of(
                                    principal.targetLanguage(),
                                    languageHelper.getFullName(principal.targetLanguage()),
                                    languageHelper.getAbbreviation(principal.targetLanguage()),
                                    languageHelper.getImagePath(principal.targetLanguage())
                            )
                    )
                    .wordsLearned(translationService.getWordsLearnedCount(
                            GetWordsLearnedCountRequest.of(
                                    principal.getUsername(),
                                    principal.targetLanguage()
                            )).value())
                    .dailyStreak(principal.dailyStreak())
                    .wordsLearnedText(localizationService.navbarLocalization(principal.sourceLanguage()).get("words"))
                    .daysSingularText(localizationService.navbarLocalization(principal.sourceLanguage()).get("days_singular"))
                    .daysPluralText(localizationService.navbarLocalization(principal.sourceLanguage()).get("days_plural"))
                    .logoutBtnText(localizationService.navbarLocalization(principal.sourceLanguage()).get("log_out"))
                    .build()
            );
            return "redirect:/";
        }
        Language sourceLanguage = getSystemLanguageInfo(httpSession);
        model.addAttribute("navbarLocalization", localizationService.navbarLocalization(sourceLanguage));
        model.addAttribute("authLocalization", localizationService.authLocalization(sourceLanguage));
        return "auth/login";
    }

    @PostMapping({"/login", "/login/"})
    public String login(@RequestParam("login") @NonNull String login,
                        @RequestParam("password") @NonNull String password,
                        Model model,
                        RedirectAttributes redirectAttributes,
                        Authentication authentication,
                        HttpServletResponse response,
                        HttpSession httpSession) {
        if(authentication != null) {
            CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
            redirectAttributes.addFlashAttribute("navbarLocalization", localizationService.navbarLocalization(principal.sourceLanguage()));
            redirectAttributes.addFlashAttribute("documentsLocalization", localizationService.documentListLocalization(principal.sourceLanguage()));
            redirectAttributes.addFlashAttribute("profilePanelAttribute", ProfilePanelAttribute.builder()
                    .languageDataList(languageHelper.getAllLanguageData())
                    .targetLanguage(LanguageData.of(
                                    principal.targetLanguage(),
                                    languageHelper.getFullName(principal.targetLanguage()),
                                    languageHelper.getAbbreviation(principal.targetLanguage()),
                                    languageHelper.getImagePath(principal.targetLanguage())
                            )
                    )
                    .wordsLearned(translationService.getWordsLearnedCount(
                            GetWordsLearnedCountRequest.of(
                                    principal.getUsername(),
                                    principal.targetLanguage()
                            )).value())
                    .dailyStreak(principal.dailyStreak())
                    .wordsLearnedText(localizationService.navbarLocalization(principal.sourceLanguage()).get("words"))
                    .daysSingularText(localizationService.navbarLocalization(principal.sourceLanguage()).get("days_singular"))
                    .daysPluralText(localizationService.navbarLocalization(principal.sourceLanguage()).get("days_plural"))
                    .logoutBtnText(localizationService.navbarLocalization(principal.sourceLanguage()).get("log_out"))
                    .build()
            );
            return "redirect:/";
        }
        ServiceResult<?> result = authService.login(LoginRequest.of(login, password), response, httpSession);
        Language sourceLanguage = getSystemLanguageInfo(httpSession);

        if (result.hasError()) {
            log.warn("login authentication failure, resending page");
            model.addAttribute("result", result);
            model.addAttribute("navbarLocalization", localizationService.navbarLocalization(sourceLanguage));
            model.addAttribute("authLocalization", localizationService.authLocalization(sourceLanguage));
            return "auth/login";
        }
        CustomUserDetails principal = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        redirectAttributes.addFlashAttribute("result", result);
        redirectAttributes.addFlashAttribute("navbarLocalization", localizationService.navbarLocalization(principal.sourceLanguage()));
        redirectAttributes.addFlashAttribute("documentsLocalization", localizationService.documentListLocalization(principal.sourceLanguage()));
        redirectAttributes.addFlashAttribute("profilePanelAttribute", ProfilePanelAttribute.builder()
                .languageDataList(languageHelper.getAllLanguageData())
                .targetLanguage(LanguageData.of(
                                principal.targetLanguage(),
                                languageHelper.getFullName(principal.targetLanguage()),
                                languageHelper.getAbbreviation(principal.targetLanguage()),
                                languageHelper.getImagePath(principal.targetLanguage())
                        )
                )
                .wordsLearned(translationService.getWordsLearnedCount(
                        GetWordsLearnedCountRequest.of(
                                principal.getUsername(),
                                principal.targetLanguage()
                        )).value())
                .dailyStreak(principal.dailyStreak())
                .wordsLearnedText(localizationService.navbarLocalization(principal.sourceLanguage()).get("words"))
                .daysSingularText(localizationService.navbarLocalization(principal.sourceLanguage()).get("days_singular"))
                .daysPluralText(localizationService.navbarLocalization(principal.sourceLanguage()).get("days_plural"))
                .logoutBtnText(localizationService.navbarLocalization(principal.sourceLanguage()).get("log_out"))
                .build()
        );
        return "redirect:/";
    }

    @GetMapping({"/signup", "/signup/"})
    public String signupPage(Model model,
                             RedirectAttributes redirectAttributes,
                             Authentication authentication,
                             HttpSession httpSession) {
        if(authentication != null) {
            CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
            redirectAttributes.addFlashAttribute("navbarLocalization", localizationService.navbarLocalization(principal.sourceLanguage()));
            redirectAttributes.addFlashAttribute("documentsLocalization", localizationService.documentListLocalization(principal.sourceLanguage()));
            redirectAttributes.addFlashAttribute("profilePanelAttribute", ProfilePanelAttribute.builder()
                    .languageDataList(languageHelper.getAllLanguageData())
                    .targetLanguage(LanguageData.of(
                                    principal.targetLanguage(),
                                    languageHelper.getFullName(principal.targetLanguage()),
                                    languageHelper.getAbbreviation(principal.targetLanguage()),
                                    languageHelper.getImagePath(principal.targetLanguage())
                            )
                    )
                    .wordsLearned(translationService.getWordsLearnedCount(
                            GetWordsLearnedCountRequest.of(
                                    principal.getUsername(),
                                    principal.targetLanguage()
                            )).value())
                    .dailyStreak(principal.dailyStreak())
                    .wordsLearnedText(localizationService.navbarLocalization(principal.sourceLanguage()).get("words"))
                    .daysSingularText(localizationService.navbarLocalization(principal.sourceLanguage()).get("days_singular"))
                    .daysPluralText(localizationService.navbarLocalization(principal.sourceLanguage()).get("days_plural"))
                    .logoutBtnText(localizationService.navbarLocalization(principal.sourceLanguage()).get("log_out"))
                    .build()
            );
            return "redirect:/";
        }
        Language sourceLanguage = getSystemLanguageInfo(httpSession);
        model.addAttribute("navbarLocalization", localizationService.navbarLocalization(sourceLanguage));
        model.addAttribute("authLocalization", localizationService.authLocalization(sourceLanguage));
        return "auth/signup";
    }

    @PostMapping({"/signup", "/signup/"})
    public String signup(@RequestParam("login") @NonNull String login,
                         @RequestParam("email") @NonNull String email,
                         @RequestParam("password") @NonNull String password,
                         Model model,
                         RedirectAttributes redirectAttributes,
                         Authentication authentication,
                         HttpSession httpSession) {
        if(authentication != null) {
            CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
            redirectAttributes.addFlashAttribute("navbarLocalization", localizationService.navbarLocalization(principal.sourceLanguage()));
            redirectAttributes.addFlashAttribute("documentsLocalization", localizationService.documentListLocalization(principal.sourceLanguage()));
            redirectAttributes.addFlashAttribute("profilePanelAttribute", ProfilePanelAttribute.builder()
                    .languageDataList(languageHelper.getAllLanguageData())
                    .targetLanguage(LanguageData.of(
                                    principal.targetLanguage(),
                                    languageHelper.getFullName(principal.targetLanguage()),
                                    languageHelper.getAbbreviation(principal.targetLanguage()),
                                    languageHelper.getImagePath(principal.targetLanguage())
                            )
                    )
                    .wordsLearned(translationService.getWordsLearnedCount(
                            GetWordsLearnedCountRequest.of(
                                    principal.getUsername(),
                                    principal.targetLanguage()
                            )).value())
                    .dailyStreak(principal.dailyStreak())
                    .wordsLearnedText(localizationService.navbarLocalization(principal.sourceLanguage()).get("words"))
                    .daysSingularText(localizationService.navbarLocalization(principal.sourceLanguage()).get("days_singular"))
                    .daysPluralText(localizationService.navbarLocalization(principal.sourceLanguage()).get("days_plural"))
                    .logoutBtnText(localizationService.navbarLocalization(principal.sourceLanguage()).get("log_out"))
                    .build()
            );
            return "redirect:/";
        }
        ServiceResult<?> result = authService.signup(SignupRequest.of(login, email, password));
        Language sourceLanguage = getSystemLanguageInfo(httpSession);

        model.addAttribute("result", result);
        if(result.hasError()) {
            log.warn("signup authentication failure");
            model.addAttribute("navbarLocalization", localizationService.navbarLocalization(sourceLanguage));
            model.addAttribute("authLocalization", localizationService.authLocalization(sourceLanguage));
            return "auth/signup";
        }
        model.addAttribute("navbarLocalization", localizationService.navbarLocalization(sourceLanguage));
        model.addAttribute("authLocalization", localizationService.authLocalization(sourceLanguage));
        return "auth/login";
    }

    @PostMapping({"/logout", "/logout/"})
    public String logout(RedirectAttributes redirectAttributes,
                         HttpServletResponse response,
                         Model model,
                         Authentication authentication,
                         HttpSession httpSession) {
        ServiceResult<?> result = authService.logout(response);
        if(result.hasError()) {
            model.addAttribute("navbarLocalization", localizationService.navbarLocalization(Language.EN));
            log.warn("logout attempt failure");
            return "/error";
        }
        Language sourceLanguage = getSystemLanguageInfo(httpSession);
        redirectAttributes.addFlashAttribute("navbarLocalization", localizationService.navbarLocalization(sourceLanguage));
        redirectAttributes.addFlashAttribute("result", result);
        redirectAttributes.addFlashAttribute("authLocalization", localizationService.authLocalization(sourceLanguage));
        return "redirect:/auth/login";
    }

    private Language getSystemLanguageInfo(HttpSession httpSession) {
        return (Language) httpSession.getAttribute("sourceLanguage");
    }
}
