@import lule.dictionary.service.translation.dto.attribute.TranslationAttribute
@import lule.dictionary.service.translation.dto.attribute.TranslationPair

@param TranslationAttribute translationAttribute

<script>
    function exitForm() {
        document.querySelector('.form').outerHTML = '';
    }
</script>
<div class="bg-secondary flex flex-col gap-2 py-2 px-6 rounded form">
    <div class="flex justify-end">
        <span class="text-red-400 font-bold text-lg cursor-pointer" onclick="exitForm()">❌</span>
    </div>
    <span class="font-bold text-lg text-accent">${translationAttribute.translation().targetWord()}</span>
    <div id="source-word-form" class="flex flex-col">
        @template.import-page.translation.update-source-words-form(
            translationPair = TranslationPair.of(translationAttribute.translation().sourceWords(), translationAttribute.translation().targetWord()),
        )
    </div>
    @template.import-page.translation.get-ai-translation-btn(targetWord = translationAttribute.translation().targetWord())
    <div class="flex flex-col gap-0.5">
        @for(var level : translationAttribute.familiarityLevels().entrySet())
            @if(level.getKey() == translationAttribute.currentFamiliarity())
                <div class="font-bold text-primary flex gap-1">
                    <span>${level.getKey()}: </span>
                    ${level.getValue().name().substring(0, 1).toUpperCase() + level.getValue().name().substring(1).toLowerCase()}
                </div>
            @else
                <div class="hover:text-primary font-bold text-neutral flex gap-1 cursor-pointer"
                     hx-put="/translations/familiarity/update"
                     hx-vals='
                     {
                        "targetWord": "${translationAttribute.translation().targetWord()}",
                        "familiarity": "${level.getValue().name()}",
                        "sourceLanguage": "${translationAttribute.translation().sourceLanguage().name()}",
                        "targetLanguage": "${translationAttribute.translation().targetLanguage().name()}",
                        "importId": "${translationAttribute.importId()}",
                        "selectedWordId": "${translationAttribute.selectedWordId()}",
                        "page": "${translationAttribute.page()}"
                     }'
                     hx-headers='js:{
                        timeZoneOffset: new Date().getTimezoneOffset()
                    }'
                     hx-target="#page-content"
                     hx-swap="outerHTML">
                    <span>${level.getKey()}: </span>
                    ${level.getValue().name().substring(0, 1).toUpperCase() + level.getValue().name().substring(1).toLowerCase()}
                </div>
            @endif
        @endfor
    </div>
</div>
