@import lule.dictionary.controller.translation.dto.TranslationModel

@param TranslationModel translationModel 

<script>
    function exitForm() {
        document.querySelector('.form').outerHTML = '';
    }
</script>
<div class="bg-secondary flex flex-col gap-1 py-2 px-6 rounded form">
    <div class="flex justify-end">
        <span class="text-red-400 font-bold text-2xl cursor-pointer" onclick="exitForm()">❌</span>
    </div>
    <span class="font-bold text-lg text-accent">${translationModel.translation().translationDetails().targetWord()}</span>
    <span class="text-accent">${translationModel.translation().translationDetails().sourceWord()}</span>
    @for(var level : translationModel.familiarityLevels().entrySet())
        @if(level.getKey() == translationModel.currentFamiliarity())
            <div class="font-bold text-tertiary flex gap-1">
                <span>${level.getKey()}: </span>
                ${level.getValue()}
            </div>
        @else
            <div class="hover:text-accent font-bold text-primary flex gap-1 cursor-pointer"
                 hx-put="/translations/familiarity/update"
                 hx-vals='
                 {
                    "sourceWord": "${translationModel.translation().translationDetails().sourceWord()}",
                    "targetWord": "${translationModel.translation().translationDetails().targetWord()}",
                    "familiarity": "${level.getValue().name()}",
                    "sourceLanguage": "${translationModel.translation().userProfileSettings().sourceLanguage().name()}",
                    "targetLanguage": "${translationModel.translation().userProfileSettings().targetLanguage().name()}",
                    "owner": "${translationModel.translation().owner()}",
                    "importId": "${translationModel.importId()}",
                    "selectedWordId": "${translationModel.selectedWordId()}"
                 }'
                 hx-target="#page-content"
                 hx-swap="outerHTML">
                <span>${level.getKey()}: </span>
                ${level.getValue()}
            </div>
        @endif
    @endfor
</div>