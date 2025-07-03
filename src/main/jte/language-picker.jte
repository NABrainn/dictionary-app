@import lule.dictionary.service.language.Language
@import java.util.List
@import java.util.Map

@param String targetLanguage
@param Map<Language, String> languages

<div>
    <label for="lang-select"></label>
    <select
    class="bg-secondary p-2 rounded"
    name="langs"
    id="lang-select">
        <option value="${targetLanguage}">${targetLanguage}</option>
        @for(var language : languages.entrySet())
            @if(!language.getValue().equals(targetLanguage))
                <option
                hx-get="/settings/changeLanguage"
                hx-vals='{
                    "lang": "${language.getKey()}"
                }'
                hx-target="#main"
                hx-swap="outerHTML"
                value="${language.getValue()}">${language.getValue()}</option>
            @endif
        @endfor
    </select>
</div>