@import lule.dictionary.language.service.LanguageData
@import java.util.List

@param String settingsText
@param String languageText
@param String uiText
@param String translationsText
@param String logoutText

@param String size
@param LanguageData userInterfaceLanguage
@param LanguageData translationLanguage
@param List<LanguageData> languages
@param String _csrf

<div class="flex justify-end items-center z-420 justify-center ${size}">
    <div>
        <a
        class="
        md:text-lg md:inline
        text-md hidden text-neutral cursor-pointer font-bold relative select-none underline"
        onclick="
        htmx.find('#settings').classList.toggle('hidden');
        htmx.findAll('.dropdown-menu').forEach(dropdown => dropdown.classList.add('hidden'))">
            <div class="flex items-center gap-1">
                <svg width="24px" height="24px" viewBox="0 0 16 16" xmlns="http://www.w3.org/2000/svg" class="fill-accent">
                    <path d="M6.50001 0H9.50001L10.0939 2.37548C10.7276 2.6115 11.3107 2.95155 11.8223 3.37488L14.1782 2.70096L15.6782 5.29904L13.9173 7.00166C13.9717 7.32634 14 7.65987 14 8C14 8.34013 13.9717 8.67366 13.9173 8.99834L15.6782 10.701L14.1782 13.299L11.8223 12.6251C11.3107 13.0484 10.7276 13.3885 10.0939 13.6245L9.50001 16H6.50001L5.90614 13.6245C5.27242 13.3885 4.68934 13.0484 4.17768 12.6251L1.82181 13.299L0.321808 10.701L2.08269 8.99834C2.02831 8.67366 2.00001 8.34013 2.00001 8C2.00001 7.65987 2.02831 7.32634 2.08269 7.00166L0.321808 5.29904L1.82181 2.70096L4.17768 3.37488C4.68934 2.95155 5.27241 2.6115 5.90614 2.37548L6.50001 0ZM8.00001 10C9.10458 10 10 9.10457 10 8C10 6.89543 9.10458 6 8.00001 6C6.89544 6 6.00001 6.89543 6.00001 8C6.00001 9.10457 6.89544 10 8.00001 10Z"/>
                </svg>
                <span>${settingsText}</span>
            </div>
        </a>
        <div class="absolute top-16 right-8 flex flex-col bg-tertiary rounded p-2 border border-2 border-secondary flex gap-4 hidden"
             id="settings">
            <div>
                <p class="font-bold text-md text-primary">${languageText}:</p>
                <div class="flex flex-col gap-2 mt-1">
                    <div class="flex justify-between gap-2 text-neutral font-bold text-md">
                        <span>${uiText}:</span>
                        <div>
                            @template.navbar.language-picker(
                            selectedLanguage = userInterfaceLanguage,
                            languages = languages,
                            type = "ui")
                        </div>
                    </div>
                    <div class="flex justify-between gap-2 text-neutral font-bold text-md">
                        <span>${translationsText}:</span>
                        <div>
                            @template.navbar.language-picker(
                            selectedLanguage = translationLanguage,
                            languages = languages,
                            type = "source")
                        </div>
                    </div>
                </div>
            </div>
            <a
            class="
            md:text-md
            text-md text-primary underline cursor-pointer font-bold"
            hx-post="/auth/logout"
            hx-target="#main"
            hx-swap="innerHTML"
            hx-push-url="/auth/login"
            hx-params="_csrf"
            hx-vals='{
                "_csrf": "${_csrf}"
            }'>
                ${logoutText}
            </a>
        </div>
    </div>
    <a
    class="
    md:text-lg md:hidden
    text-md inline text-neutral cursor-pointer font-bold"
    onclick="
        htmx.find('#settings').classList.toggle('hidden');
        htmx.findAll('.dropdown-menu').forEach(dropdown => dropdown.classList.add('hidden'))">
        <svg width="24px" height="24px" viewBox="0 0 16 16" xmlns="http://www.w3.org/2000/svg" class="fill-accent">
            <path d="M6.50001 0H9.50001L10.0939 2.37548C10.7276 2.6115 11.3107 2.95155 11.8223 3.37488L14.1782 2.70096L15.6782 5.29904L13.9173 7.00166C13.9717 7.32634 14 7.65987 14 8C14 8.34013 13.9717 8.67366 13.9173 8.99834L15.6782 10.701L14.1782 13.299L11.8223 12.6251C11.3107 13.0484 10.7276 13.3885 10.0939 13.6245L9.50001 16H6.50001L5.90614 13.6245C5.27242 13.3885 4.68934 13.0484 4.17768 12.6251L1.82181 13.299L0.321808 10.701L2.08269 8.99834C2.02831 8.67366 2.00001 8.34013 2.00001 8C2.00001 7.65987 2.02831 7.32634 2.08269 7.00166L0.321808 5.29904L1.82181 2.70096L4.17768 3.37488C4.68934 2.95155 5.27241 2.6115 5.90614 2.37548L6.50001 0ZM8.00001 10C9.10458 10 10 9.10457 10 8C10 6.89543 9.10458 6 8.00001 6C6.89544 6 6.00001 6.89543 6.00001 8C6.00001 9.10457 6.89544 10 8.00001 10Z"/>
        </svg>
    </a>
</div>