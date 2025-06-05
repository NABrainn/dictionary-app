@import lule.dictionary.dto.application.interfaces.translation.Translation
@import lule.dictionary.enumeration.Familiarity
@import java.util.SortedMap

@param int importId
@param int selectedWordId
@param int currentFamiliarity
@param Translation translation
@param SortedMap<Integer, Familiarity> familiarityLevels

<script>
    function exitForm() {
        document.querySelector('.form').outerHTML = '';
    }
</script>
<div class="bg-secondary flex flex-col gap-1 py-2 px-6 rounded form">
    <div class="flex justify-end">
        <span class="text-red-400 font-bold text-2xl cursor-pointer" onclick="exitForm()">❌</span>
    </div>
    <span class="font-bold text-lg text-accent">${translation.translationDetails().targetWord()}</span>
    <span class="text-accent">${translation.translationDetails().sourceWord()}</span>
    @for(var level : familiarityLevels.entrySet())
        @if(level.getKey() == currentFamiliarity)
            <div class="font-bold text-tertiary flex gap-1">
                <span>${level.getKey()}: </span>
                ${level.getValue()}
            </div>
        @else
            <div class="hover:text-accent font-bold text-primary flex gap-1 cursor-pointer"
                 hx-put="/translations/familiarity/update"
                 hx-vals='
                 {
                    "sourceWord": "${translation.translationDetails().sourceWord()}",
                    "targetWord": "${translation.translationDetails().targetWord()}",
                    "familiarity": "${level.getValue().name()}",
                    "sourceLanguage": "${translation.userProfileSettings().sourceLanguage().name()}",
                    "targetLanguage": "${translation.userProfileSettings().targetLanguage().name()}",
                    "owner": "${translation.owner()}",
                    "importId": "${importId}",
                    "selectedWordId": "${selectedWordId}"
                 }'
                 hx-target="#page-content"
                 hx-swap="outerHTML">
                <span>${level.getKey()}: </span>
                ${level.getValue()}
            </div>
        @endif
    @endfor
</div>