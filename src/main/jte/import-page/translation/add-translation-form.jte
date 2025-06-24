@import lule.dictionary.service.translation.dto.TranslationModel

@param TranslationModel translationModel

<script>
    function exitForm() {
        document.querySelector('.form').outerHTML = '';
    }
</script>
<div class="bg-secondary flex flex-col gap-1 py-2 px-6 rounded form">
    <div class="flex justify-end">
        <span class="text-red-400 font-bold text-lg cursor-pointer" onclick="exitForm()">❌</span>
    </div>
    <span class="font-bold text-lg text-accent">${translationModel.translation().targetWord()}</span>
    @template.import-page.translation.add-source-words-form(
        targetWord = translationModel.translation().targetWord(),
        sourceLanguage = translationModel.translation().sourceLanguage().name(),
        targetLanguage = translationModel.translation().targetLanguage().name(),
        importId = translationModel.importId(),
        selectedWordId = translationModel.selectedWordId(),
        sourceWords = translationModel.translation().sourceWords()
    )
    @for(var level : translationModel.familiarityLevels().entrySet())
        <div class="hover:text-primary font-bold text-neutral flex gap-1 cursor-pointer"
             hx-post="/translations/new"
             hx-vals='
             {
                "sourceWords": "${translationModel.translation().sourceWords().getFirst()}",
                "targetWord": "${translationModel.translation().targetWord()}",
                "familiarity": "${level.getValue().name()}",
                "sourceLanguage": "${translationModel.translation().sourceLanguage().name()}",
                "targetLanguage": "${translationModel.translation().targetLanguage().name()}",
                "importId": "${translationModel.importId()}",
                "selectedWordId": "${translationModel.selectedWordId()}"
             }'
             hx-target="#page-content"
             hx-swap="outerHTML">
            <span>${level.getKey()}: </span>
            ${level.getValue()}
        </div>
    @endfor
</div>
