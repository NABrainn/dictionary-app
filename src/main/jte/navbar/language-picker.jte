@import lule.dictionary.language.service.LanguageData
@import java.util.List

@param LanguageData selectedLanguage
@param List<LanguageData> languages
@param String type

<div class="relative text-left text-md w-16">
        <button
        type="button"
        class="flex font-bold w-full cursor-default gap-2 items-center rounded-md bg-accent pl-1 text-left text-primary px-6
        sm:text-sm/10"
        aria-haspopup="listbox"
        aria-expanded="true"
        aria-labelledby="listbox-label"
        id="dropdown-btn"
        onclick="
        [...htmx.findAll('.dropdown-menu')]
            .filter(dropdown => dropdown.id !== 'dropdown-menu-${type}')
            .forEach(dropdown => dropdown.classList.add('hidden'))
        htmx.find('#dropdown-menu-${type}').classList.toggle('hidden')">
            <img
            class="size-5"
            src="${selectedLanguage.imgPath()}" alt="image showcasing flag of selected language">
            <span
            class="
            md:text-lg
            text-md">${selectedLanguage.languageCode().toUpperCase()}</span>
        </button>

        <div id="dropdown-menu-${type}" class="dropdown-menu hidden mt-1.5 left-0 absolute bg-accent w-full text-left text-primary rounded-md z-10 py-2 gap-2">
            <div class="flex flex-col gap-1">
                @for(var language : languages)
                    @if(!language.languageCode().equals(selectedLanguage.languageCode()))
                        <div
                        class="
                        hover:bg-primary/80 hover:text-accent
                        flex gap-2 pl-1 font-bold cursor-pointer"
                        hx-get="/settings/changeLanguage/${type}"
                        hx-vals='{
                            "lang": "${language.language()}"
                        }'
                        hx-target="#main"
                        hx-swap="outerHTML"
                        hx-push-url="true"
                        data-value="${language.fullName()}">
                            <img
                            class="size-5"
                            src="${language.imgPath()}" alt="image showcasing flag of selected language">
                            <span class="text-md">${language.languageCode().toUpperCase()}</span>
                        </div>
                    @endif
                @endfor
            </div>
        </div>
</div>

<!--PURELY FOR SEMANTICS-->
<div class="hidden">
    <label for="lang-select"></label>
    <select
    class="hidden" aria-hidden="true"
    name="langs"
    id="lang-select">
    <option
    value="${selectedLanguage.fullName()}">${selectedLanguage.languageCode().toUpperCase()}</option>
        @for(var language : languages)
            @if(!language.languageCode().equals(selectedLanguage))
                <option
                hx-get="/settings/changeLanguage"
                hx-vals='{
                    "lang": "${language.language()}"
                }'
                hx-target="#main"
                hx-swap="outerHTML"
                hx-push-url="true"
                value="${language.fullName()}">${language.languageCode().toUpperCase()}</option>
            @endif
        @endfor
    </select>
</div>

