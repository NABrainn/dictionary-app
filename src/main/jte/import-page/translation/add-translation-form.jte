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
        <div class="hover:text-accent font-bold text-primary flex gap-1 cursor-pointer"
             hx-post="/translations/new"
             hx-vals='
             {
                "sourceWord": "${translationModel.translation().translationDetails().sourceWord()}",
                "targetWord": "${translationModel.translation().translationDetails().targetWord()}",
                "familiarity": "${level.getValue().name()}",
                "sourceLanguage": "${translationModel.translation().userProfileSettings().sourceLanguage().name()}",
                "targetLanguage": "${translationModel.translation().userProfileSettings().targetLanguage().name()}",
                "importId": "${translationModel.importId()}",
                "selectedWordId": "${translationModel.selectedWordId()}"
             }'
             hx-target="#main"
             hx-swap="outerHTML">
            <span>${level.getKey()}: </span>
            ${level.getValue()}
        </div>
    @endfor
</div>