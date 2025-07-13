@import lule.dictionary.dto.application.LanguageData
@import lule.dictionary.service.language.Language
@import java.util.List
@import java.util.Map

@param String targetLanguage
@param List<LanguageData> languages

<div>
    <label for="lang-select"></label>
    <select
    class="bg-secondary p-2 rounded text-accent"
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