@import lule.dictionary.service.translation.dto.attribute.TranslationAttribute
@import lule.dictionary.service.translation.dto.attribute.TranslationPair
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
    <div id="source-word-form" class="flex flex-col">
    @template.document-page.content.translation.add.add-source-words-form(
        placeholderText = translationLocalization.get("add_translation"),
        sourceLanguage = translationAttribute.translation().sourceLanguage().name(),
        targetLanguage = translationAttribute.translation().targetLanguage().name(),
        importId = translationAttribute.importId(),
        selectedWordId = translationAttribute.selectedWordId(),
        translationPair = TranslationPair.of(
            translationAttribute.translation().sourceWords(),
            translationAttribute.translation().targetWord()
        ),
    )
    </div>
    @template.document-page.content.translation.ai-btn(
        text = translationLocalization.get("ai_translation"),
        targetWord = translationAttribute.translation().targetWord()
    )
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
                "selectedWordId": "${translationAttribute.selectedWordId()}"
            }'
            hx-target="#translation-form-container-${translationAttribute.selectedWordId()}"
            hx-headers='js:{
                timeZoneOffset: new Date().getTimezoneOffset()
            }'
            hx-swap="innerHTML"
            hx-on::before-request="htmx.find('word-highlighter').highlightWords(${translationAttribute.selectedWordId()}, '${level.getValue().name().toLowerCase()}')">
                <span class="cursor-pointer">${level.getKey()}: </span>
                @for(var word : translationAttribute.translation().sourceWords())
                    <input type="hidden" name="sourceWords" value="${word}">
                @endfor

                <input
                class=" cursor-pointer"
                value="${translationLocalization.get(level.getValue().name().toLowerCase())}"
                type="submit">
            </form>
        @endfor
    </div>
</div>
