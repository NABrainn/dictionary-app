@import lule.dictionary.service.translation.dto.attribute.TranslationAttribute
@import java.util.Map

@param TranslationAttribute translationAttribute
@param String targetWord
@param Map<String, String> translationLocalization

!{var processedWord = translationAttribute.translation().targetWord().replaceAll("[^\\p{L}\\p{N}\\s-]", "").toLowerCase();}
!{String hxTarget = translationAttribute.isPhrase() ? "#selected-phrase-form" : "#translation-form-container-" + translationAttribute.selectedWordId();}
<div
onclick="event.stopPropagation()"
class="translation-form-${translationAttribute.selectedWordId()} bg-tertiary flex flex-col gap-0.75 p-3 rounded form max-w-45 pointer-events-auto"
id="translation-form">
    <div class="flex justify-end">
        <span
        class="md:text-md
        text-sm text-red-400 font-bold cursor-pointer"
        onclick="htmx.find('.form').outerHTML = ''">❌</span>
    </div>
    <span class="font-bold text-lg text-accent text-wrap">${processedWord}</span>
    <div id="translation-manager">
        @template.document-page.content.translation.add.translation-manager(
            translationAttribute = translationAttribute,
            translationLocalization = translationLocalization
        )
    </div>
    <div class="flex flex-col gap-0.25">
        @for(var level : translationAttribute.familiarityLevels().entrySet())
            <form
            class="flex gap-1 cursor-pointer font-bold text-neutral hover:text-primary"
            id="translation-form-${translationAttribute.selectedWordId()}"
            hx-post="/translations/new"
            hx-vals='{
                "targetWord": "${processedWord}",
                "familiarity": "${level.getValue().name()}",
                "sourceLanguage": "${translationAttribute.translation().sourceLanguage().name()}",
                "targetLanguage": "${translationAttribute.translation().targetLanguage().name()}",
                "documentId": "${translationAttribute.documentId()}",
                "selectedWordId": "${translationAttribute.selectedWordId()}",
                "isPhrase": "${translationAttribute.isPhrase()}"
            }'
            hx-target="#translation-form-container-${translationAttribute.selectedWordId()}"
            hx-headers='js:{
                timeZoneOffset: new Date().getTimezoneOffset()
            }'
            hx-swap="innerHTML"
            hx-on::before-request="
            htmx.findAll('.selectable-state-${processedWord.replace(" ", "-")}').forEach(input => input.dataset.isFound = true)
            htmx.find('word-highlighter').highlightWords('${processedWord}', '${level.getValue().name().toLowerCase()}');
            const highlighter = htmx.find('phrase-highlighter');
            highlighter.highlightPhrases('${processedWord}', '${level.getValue().name()}');
            highlighter.markPhrasesAsSaved();
            ">
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
