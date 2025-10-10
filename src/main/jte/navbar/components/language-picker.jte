@import lule.dictionary.controllerAdvice.data.navbar.LanguageOption
@import lule.dictionary.language.service.Language
@import java.util.List

@param Language selectedLanguage
@param List<LanguageOption> languages
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
        src="${languages.stream()
                .filter(option -> option.language().equals(selectedLanguage))
                .findFirst()
                .map(LanguageOption::imgUrl)
                .orElse("")}"
        alt="image showcasing flag of selected language">
        <span
        class="
        md:text-lg
        text-md">${selectedLanguage.name().toUpperCase()}</span>
    </button>

    <div 
    id="dropdown-menu-${type}" 
    class="dropdown-menu hidden mt-1.5 left-0 absolute bg-accent w-full text-left text-primary rounded-md z-10 py-2 gap-2">
        <div class="flex flex-col gap-1">
            @for(var languageDetails : languages)
                @if(!languageDetails.language().equals(selectedLanguage))
                    <div
                    class="
                    hover:bg-primary/80 hover:text-accent
                    flex gap-2 pl-1 font-bold cursor-pointer"
                    hx-get="/nav/changeLanguage"
                    hx-vals='{
                        "type": "${type}",
                        "lang": "${languageDetails.language()}"
                    }'
                    hx-target="#main"
                    hx-swap="outerHTML"
                    hx-push-url="true">
                        <img
                        class="size-5"
                        src="${languageDetails.imgUrl()}" alt="image showcasing flag of selected language">
                        <span class="text-md">${languageDetails.language().name().toUpperCase()}</span>
                    </div>
                @endif
            @endfor
        </div>
    </div>
</div>

