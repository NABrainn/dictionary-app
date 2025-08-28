@import lule.dictionary.enumeration.Familiarity

@param int wordId
@param int documentId
@param String word
@param String processedWord
@param String bgColor = ""
@param String textColor = ""
@param boolean isSelectable = true
@param boolean isPhraseWord = false
@param Familiarity familiarity = null
@param boolean isFound = false

!{var phraseWord = "";}
@if(isPhraseWord)
    !{phraseWord = "phrase-word";}
@endif

!{var familiarityString = "unknown";}
@if(familiarity != null)
    !{familiarityString = familiarity.name().toLowerCase();
}
@endif

<input
id="selectable-input-${wordId}"
type="hidden"
data-is-found="${isFound}"
class="selectable-state-${processedWord}">

@if(isSelectable)
    <div
    class="
    border border-2 rounded-none border-transparent inline-block relative cursor-pointer rounded whitespace-nowrap text-lg"
    id="word-${wordId}"

    data-is-wrapped="false"
    data-familiarity="${familiarityString}"
    data-is-word-selectable="true"
    data-is-word-selected="false"
    data-type="word"
    data-value="${processedWord}"
    data-id="${wordId}"
    data-is-selectable="true"

    hx-get="/translations/find"
    hx-target="#translation-form-container-${wordId}"
    hx-swap="innerHTML"
    hx-vals='js:{
        documentId: ${documentId},
        targetWord: "${processedWord}",
        selectedWordId: ${wordId},
        isPhrase: false,
        isFound: htmx.find("#selectable-input-${wordId}").dataset.isFound ?? false
    }'
    hx-trigger="click"
    hx-on::before-request="
    htmx.find('phrase-highlighter').highlightSavedPhrases();
    htmx.find('word-highlighter').updateSelectableId(${wordId})">
        <div
        onclick="event.stopPropagation()"
        class="
        cursor-default z-2 absolute inline left-2 top-10"
        id="translation-form-container-${wordId}"

        data-is-translation-form-container="true"
        hx-on::after-swap="htmx.find('form-position-adjuster').updatePositions(${wordId})">
        </div>
        <span
        class="${bgColor} ${textColor} rounded"
        id="word-span-${wordId}"

        data-familiarity="${familiarityString}"
        data-is-word-selected="false"
        data-value="${processedWord}"
        data-type="word"
        data-subtype="word-span"
        data-is-word-text="true">${word}</span>
    </div>
@else
    <div
    class="
    inline-block relative rounded whitespace-nowrap text-lg pointer-events-none select-none">
        <span
        class="${bgColor} ${textColor} rounded ${phraseWord}">${word}</span>
    </div>
@endif
