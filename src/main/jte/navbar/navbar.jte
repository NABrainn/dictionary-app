@import lule.dictionary.dto.application.LanguageData
@import java.util.List
@import java.util.Map

@param String username
@param LanguageData sourceLanguage
@param LanguageData targetLanguage
@param boolean authenticated
@param int wordsLearned
@param int dailyStreak
@param List<LanguageData> languageData

@param Map<String, String> navbarLocalization


<nav
class="
md:justify-between md:p-4 md:py-2
gap-2 justify-around px-0.5 py-1 flex bg-secondary items-center min-h-18">
    <div
    class="flex items-center align-center text-lg text-neutral">
        <a
        hx-get="/"
        hx-swap="innerHTML"
        hx-target="#main"
        hx-push-url="true"
        class="
        md:text-lg md:inline
        hidden text-md cursor-pointer underline font-bold">${navbarLocalization.get("home")}</a>
        <a
        hx-get="/"
        hx-swap="innerHTML"
        hx-target="#main"
        hx-push-url="true"
        class="
        md:text-lg md:hidden
        inline text-lg cursor-pointer font-bold">
            <svg class="fill-accent" fill="currentColor" width="24px" height="24px" viewBox="0 0 16 16" xmlns="http://www.w3.org/2000/svg">
                <path d="M1 6V15H6V11C6 9.89543 6.89543 9 8 9C9.10457 9 10 9.89543 10 11V15H15V6L8 0L1 6Z"/>
            </svg>
        </a>
    </div>
    @if(authenticated)
        <div class="flex gap-3 items-center">
            <div
            class="
            md:gap-6 md:p-2
            gap-2 p-1.25 flex items-center bg-secondary rounded select-none">
                @template.navbar.language-picker(
                    targetLanguage = targetLanguage,
                    languages = languageData
                )
                <span
                class="
                md:text-lg
                text-lg font-bold text-neutral text-accent">🧠 ${navbarLocalization.get("words")}: ${wordsLearned}</span>
                @if(dailyStreak == 1)
                    <span
                    class="
                    md:text-lg
                    text-lg font-bold text-neutral text-accent">💪 ${dailyStreak} ${navbarLocalization.get("days_singular")}</span>
                @else
                    <span
                    class="
                    md:text-lg
                    text-lg font-bold text-neutral text-accent">💪${dailyStreak} ${navbarLocalization.get("days_plural")}</span>
                @endif
            </div>

        </div>
        <div class="fles justify-center">
            <a
            class="
            md:text-lg md:inline
            text-md hidden text-neutral underline cursor-pointer font-bold"
            hx-post="/auth/logout"
            hx-target="#main"
            hx-swap="innerHTML"
            hx-push-url="/auth/login">
                ${navbarLocalization.get("log_out")}
            </a>
            <a
            class="
            md:text-lg md:hidden
            text-md inline text-neutral cursor-pointer font-bold"
            hx-post="/auth/logout"
            hx-target="#main"
            hx-swap="innerHTML"
            hx-push-url="/auth/login">
                <svg class="fill-accent" width="24px" height="24px" viewBox="0 0 16 16" xmlns="http://www.w3.org/2000/svg">
                    <path d="M6.50001 0H9.50001L10.0939 2.37548C10.7276 2.6115 11.3107 2.95155 11.8223 3.37488L14.1782 2.70096L15.6782 5.29904L13.9173 7.00166C13.9717 7.32634 14 7.65987 14 8C14 8.34013 13.9717 8.67366 13.9173 8.99834L15.6782 10.701L14.1782 13.299L11.8223 12.6251C11.3107 13.0484 10.7276 13.3885 10.0939 13.6245L9.50001 16H6.50001L5.90614 13.6245C5.27242 13.3885 4.68934 13.0484 4.17768 12.6251L1.82181 13.299L0.321808 10.701L2.08269 8.99834C2.02831 8.67366 2.00001 8.34013 2.00001 8C2.00001 7.65987 2.02831 7.32634 2.08269 7.00166L0.321808 5.29904L1.82181 2.70096L4.17768 3.37488C4.68934 2.95155 5.27241 2.6115 5.90614 2.37548L6.50001 0ZM8.00001 10C9.10458 10 10 9.10457 10 8C10 6.89543 9.10458 6 8.00001 6C6.89544 6 6.00001 6.89543 6.00001 8C6.00001 9.10457 6.89544 10 8.00001 10Z"/>
                </svg>
            </a>
        </div>
    @else
        <div class="flex justify-center">
            <a
            class="md:text-lg md:inline
            text-lg hidden text-neutral underline cursor-pointer font-bold"
            hx-get="/auth/login"
            hx-target="#main"
            hx-swap="innerHTML"
            hx-push-url="true">
                ${navbarLocalization.get("log_in")}
            </a>
            <a
            class="md:text-lg md:invisible
            text-lg inline text-neutral cursor-pointer font-bold"
            hx-get="/auth/login"
            hx-target="#main"
            hx-swap="innerHTML"
            hx-push-url="true">
                <svg class="fill-accent" width="24px" height="24px" viewBox="0 0 16 16" xmlns="http://www.w3.org/2000/svg">
                    <path d="M6.50001 0H9.50001L10.0939 2.37548C10.7276 2.6115 11.3107 2.95155 11.8223 3.37488L14.1782 2.70096L15.6782 5.29904L13.9173 7.00166C13.9717 7.32634 14 7.65987 14 8C14 8.34013 13.9717 8.67366 13.9173 8.99834L15.6782 10.701L14.1782 13.299L11.8223 12.6251C11.3107 13.0484 10.7276 13.3885 10.0939 13.6245L9.50001 16H6.50001L5.90614 13.6245C5.27242 13.3885 4.68934 13.0484 4.17768 12.6251L1.82181 13.299L0.321808 10.701L2.08269 8.99834C2.02831 8.67366 2.00001 8.34013 2.00001 8C2.00001 7.65987 2.02831 7.32634 2.08269 7.00166L0.321808 5.29904L1.82181 2.70096L4.17768 3.37488C4.68934 2.95155 5.27241 2.6115 5.90614 2.37548L6.50001 0ZM8.00001 10C9.10458 10 10 9.10457 10 8C10 6.89543 9.10458 6 8.00001 6C6.89544 6 6.00001 6.89543 6.00001 8C6.00001 9.10457 6.89544 10 8.00001 10Z"/>
                </svg>
            </a>
        </div>
    @endif
</nav>