@import lule.dictionary.service.translation.dto.attribute.TranslationAttribute
@import java.util.Map

@param TranslationAttribute translationAttribute
@param Map<String, String> translationLocalization

<div
onclick="event.stopPropagation()"
class="translation-form-${translationAttribute.selectedWordId()} bg-tertiary flex flex-col gap-0.75 p-3 rounded form max-w-45">
    <div class="flex justify-end">
        <span
        class="md:text-md
        text-sm text-red-400 font-bold cursor-pointer" onclick="htmx.find('.form').outerHTML = ''">❌</span>
    </div>
    <span class="font-bold text-lg text-accent">${translationAttribute.translation().targetWord()}</span>
    <div id="translation-manager">
        @template.document-page.content.translation.update.translation-manager(
            translationAttribute = translationAttribute,
            translationLocalization = translationLocalization
        )
    </div>
    <div class="flex flex-col gap-0.25">
        @for(var level : translationAttribute.familiarityLevels().entrySet())
            @if(level.getKey() == translationAttribute.currentFamiliarity())
                <div class="font-bold text-primary flex gap-1">
                    <span>${level.getKey()}: </span>
                    <span>${translationLocalization.get(level.getValue().name().toLowerCase())}</span>
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
                "selectedWordId": "${translationAttribute.selectedWordId()}"
            }'
            hx-headers='js:{
                timeZoneOffset: new Date().getTimezoneOffset()
            }'
            hx-target="#translation-form-container-${translationAttribute.selectedWordId()}"
            hx-swap="innerHTML"
            hx-on::before-request="htmx.find('word-highlighter').highlightWords(${translationAttribute.selectedWordId()}, '${level.getValue().name().toLowerCase()}')">
                <span>${level.getKey()}: </span>
                <input
                class=" cursor-pointer"
                value="${translationLocalization.get(level.getValue().name().toLowerCase())}"
                type="submit">
            </form>
            @endif
        @endfor
    </div>
</div>
