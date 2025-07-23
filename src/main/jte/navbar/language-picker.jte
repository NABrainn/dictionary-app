@import lule.dictionary.dto.application.LanguageData
@import java.util.List

@param LanguageData targetLanguage
@param List<LanguageData> languages
<div class="relative text-left text-md animate-fade">
        <button
        type="button"
        class="flex font-bold w-full cursor-default gap-2 items-center rounded-md bg-accent pl-1 text-left text-primary px-6
        sm:text-sm/10"
        aria-haspopup="listbox"
        aria-expanded="true"
        aria-labelledby="listbox-label"
        id="dropdown-btn"
        onclick="htmx.find('#dropdown-menu').classList.toggle('hidden')">
            <img
            class="size-5"
            src="${targetLanguage.imgPath()}" alt="image showcasing flag of selected language">
            <span
            class="
            md:text-lg
            text-md">${targetLanguage.libreTranslateAbbreviation().toUpperCase()}</span>
        </button>

        <div id="dropdown-menu" class="hidden mt-1.5 left-0 absolute bg-accent w-full text-left text-primary rounded-md z-10 py-2">
            <div class="flex flex-col gap-1">
                @for(var language : languages)
                    @if(!language.libreTranslateAbbreviation().equals(targetLanguage.libreTranslateAbbreviation()))
                        <div
                        class="
                        hover:bg-primary/80 hover:text-accent
                        flex gap-2 pl-1 font-bold cursor-pointer"
                        hx-get="/settings/changeLanguage"
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
                            <span class="text-md">${language.libreTranslateAbbreviation().toUpperCase()}</span>
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
                value="${targetLanguage.fullName()}">${targetLanguage.libreTranslateAbbreviation().toUpperCase()}</option>
        @for(var language : languages)
            @if(!language.libreTranslateAbbreviation().equals(targetLanguage))
                <option
                        hx-get="/settings/changeLanguage"
                        hx-vals='{
                    "lang": "${language.language()}"
                }'
                        hx-target="#main"
                        hx-swap="outerHTML"
                        hx-push-url="true"
                        value="${language.fullName()}">${language.libreTranslateAbbreviation().toUpperCase()}</option>
            @endif
        @endfor
    </select>
</div>

