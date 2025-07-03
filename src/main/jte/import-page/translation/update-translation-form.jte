@import lule.dictionary.service.dto.ServiceResult
@import lule.dictionary.service.translation.dto.TranslationModel
@import java.util.Map

@param TranslationModel translationModel 

<script>
    function exitForm() {
        document.querySelector('.form').outerHTML = '';
    }
</script>
<div class="bg-secondary flex flex-col gap-2 py-2 px-6 rounded form">
    <div class="flex justify-end">
        <span class="text-red-400 font-bold text-lg cursor-pointer" onclick="exitForm()">❌</span>
    </div>
    <span class="font-bold text-lg text-accent">${translationModel.translation().targetWord()}</span>
    <div id="source-word-form" class="flex flex-col">
        @template.import-page.translation.update-source-words-form(
        sourceWords = translationModel.translation().sourceWords(),
        targetWord = translationModel.translation().targetWord(),
        result = new ServiceResult(false, Map.of()))
    </div>
    @template.import-page.translation.get-ai-translation-btn(targetWord = translationModel.translation().targetWord())
    <div class="flex flex-col gap-0.5">
        @for(var level : translationModel.familiarityLevels().entrySet())
            @if(level.getKey() == translationModel.currentFamiliarity())
                <div class="font-bold text-primary flex gap-1">
                    <span>${level.getKey()}: </span>
                    ${level.getValue()}
                </div>
            @else
                <div class="hover:text-primary font-bold text-neutral flex gap-1 cursor-pointer"
                     hx-put="/translations/familiarity/update"
                     hx-vals='
                     {
                        "targetWord": "${translationModel.translation().targetWord()}",
                        "familiarity": "${level.getValue().name()}",
                        "sourceLanguage": "${translationModel.translation().sourceLanguage().name()}",
                        "targetLanguage": "${translationModel.translation().targetLanguage().name()}",
                        "importId": "${translationModel.importId()}",
                        "selectedWordId": "${translationModel.selectedWordId()}",
                        "page": "${translationModel.page()}"
                     }'
                     hx-headers='js:{
                        timeZoneOffset: new Date().getTimezoneOffset()
                    }'
                     hx-target="#page-content"
                     hx-swap="outerHTML">
                    <span>${level.getKey()}: </span>
                    ${level.getValue()}
                </div>
            @endif
        @endfor
    </div>
</div>
