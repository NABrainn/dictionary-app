@import lule.dictionary.service.translation.dto.attribute.TranslationAttribute
@import lule.dictionary.service.translation.dto.attribute.TranslationPair

@param TranslationAttribute translationAttribute
<div
onclick="event.stopPropagation()"
class="open bg-secondary flex flex-col gap-1.5 p-3 rounded form max-w-45 animate-fade">
    <div class="flex justify-end">
        <span class="text-red-400 font-bold text-lg cursor-pointer" onclick="htmx.find('.form').outerHTML = ''">❌</span>
    </div>
    <span class="font-bold text-lg text-accent">${translationAttribute.translation().targetWord()}</span>
    <div id="source-word-form" class="flex flex-col">
    @template.import-page.translation.add-source-words-form(
        sourceLanguage = translationAttribute.translation().sourceLanguage().name(),
        targetLanguage = translationAttribute.translation().targetLanguage().name(),
        importId = translationAttribute.importId(),
        selectedWordId = translationAttribute.selectedWordId(),
        page = translationAttribute.page(),
        translationPair = TranslationPair.of(
            translationAttribute.translation().sourceWords(),
            translationAttribute.translation().targetWord()
        ),
        formPositionData = translationAttribute.formPositionData()
    )
    </div>
    @template.import-page.translation.get-ai-translation-btn(targetWord = translationAttribute.translation().targetWord())
    <div class="flex flex-col gap-0.25">
        @for(var level : translationAttribute.familiarityLevels().entrySet())
            <form
            class="flex gap-1 cursor-pointer font-bold text-neutral hover:text-primary"
            id="translation-form-${translationAttribute.selectedWordId()}"
            hx-post="/translations/new"
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
            hx-target="#page-content"
            hx-headers='js:{
                timeZoneOffset: new Date().getTimezoneOffset()
            }'
            hx-swap="outerHTML">
                <span class="cursor-pointer">${level.getKey()}: </span>
                @for(var word : translationAttribute.translation().sourceWords())
                    <input type="hidden" name="sourceWords" value="${word}">
                @endfor
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
        @endfor
    </div>
</div>
