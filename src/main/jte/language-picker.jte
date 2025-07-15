@import lule.dictionary.dto.application.LanguageData
@import java.util.List

@param String targetLanguage
@param List<LanguageData> languages

<div>
    <label for="lang-select"></label>
    <select
    class="
    md:px-2 md:py-2 md:text-lg
    px-0.5 py-1 text-md rounded text-primary bg-tertiary  font-bold"
    name="langs"
    id="lang-select">
        <option
        value="${targetLanguage}">${targetLanguage}</option>
        @for(var language : languages)
            @if(!language.fullName().equals(targetLanguage))
                <option
                hx-get="/settings/changeLanguage"
                hx-vals='{
                    "lang": "${language.language()}"
                }'
                hx-target="#main"
                hx-swap="outerHTML"
                hx-push-url="true"
                value="${language.fullName()}">${language.fullName()}</option>
            @endif
        @endfor
    </select>
</div>