@import lule.dictionary.service.translation.dto.attribute.TranslationAttribute
@import lule.dictionary.service.translation.dto.attribute.TranslationPair

@param TranslationAttribute translationAttribute

<div
onclick="event.stopPropagation()"
class="translation-form-${translationAttribute.selectedWordId()} bg-secondary flex flex-col gap-1.5 p-3 rounded form max-w-45 animate-fade">
    <div class="flex justify-end">
        <span
        class="md:text-lg
        text-md text-red-400 font-bold cursor-pointer" onclick="htmx.find('.form').outerHTML = ''">❌</span>
    </div>
    <span class="font-bold text-lg text-accent">${translationAttribute.translation().targetWord()}</span>
    <div id="source-word-form" class="flex flex-col">
        @template.document-page.translation.update.update-source-words-form(
            translationPair = TranslationPair.of(translationAttribute.translation().sourceWords(), translationAttribute.translation().targetWord()),
        )
    </div>
    @template.document-page.translation.get-ai-translation-btn(targetWord = translationAttribute.translation().targetWord())
    <div class="flex flex-col gap-0.25">
        @for(var level : translationAttribute.familiarityLevels().entrySet())
            @if(level.getKey() == translationAttribute.currentFamiliarity())
                <div class="font-bold text-primary flex gap-1">
                    <span>${level.getKey()}: </span>
                    ${level.getValue().name().substring(0, 1).toUpperCase() + level.getValue().name().substring(1).toLowerCase()}
                </div>
            @else
            <form
            class="hover:text-primary font-bold text-neutral flex gap-1"
            id="translation-form-${translationAttribute.selectedWordId()}"
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
                @if(translationAttribute.formPositionData() != null)
                    <input type="hidden" name="left" class="left-${translationAttribute.selectedWordId()}" value="${translationAttribute.formPositionData().left()}">
                    <input type="hidden" name="right" class="right-${translationAttribute.selectedWordId()}" value="${translationAttribute.formPositionData().right()}">
                    <input type="hidden" name="top" class="top-${translationAttribute.selectedWordId()}" value="${translationAttribute.formPositionData().top()}">
                    <input type="hidden" name="bottom" class="bottom-${translationAttribute.selectedWordId()}" value="${translationAttribute.formPositionData().bottom()}">
                @else
                    <input type="hidden" name="left" class="left-${translationAttribute.selectedWordId()}">
                    <input type="hidden" name="right" class="right-${translationAttribute.selectedWordId()}">
                    <input type="hidden" name="top" class="top-${translationAttribute.selectedWordId()}">
                    <input type="hidden" name="bottom" class="bottom-${translationAttribute.selectedWordId()}">
                @endif
                    <input
                    class=" cursor-pointer"
                    value="${level.getValue().name().substring(0, 1).toUpperCase() + level.getValue().name().substring(1).toLowerCase()}"
                    type="submit">
            </form>
            @endif
        @endfor
    </div>
</div>
