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
@param Familiarity phraseFamiliarity = null
@param boolean isFound = false
@param boolean isWrapped = false

!{var familiarityString = "unknown";}
@if(familiarity != null)
    !{familiarityString = familiarity.name().toLowerCase();}
@endif
!{String borderStyles = isWrapped ? "pointer-events-none" : "border border-2  border-transparent";}
!{String isWrappedData = isWrapped  ? "true" : "false";}
!{String isSavedData = isFound  ? "true" : "false";}


<input
id="selectable-input-${wordId}"
type="hidden"

data-is-found="${isFound}">

@if(isSelectable)
    <div
    class="
    ${borderStyles} inline-block rounded-none relative cursor-pointer rounded whitespace-nowrap text-lg"
    id="word-${wordId}"

    data-id="${wordId}"
    data-value="${processedWord}"
    data-familiarity="${familiarityString}"
    data-document-id="${documentId}"

    data-is-selectable="true"
    data-is-selected="false"
    data-is-word="true"
    data-is-saved="${isSavedData}"
    data-is-wrapped="${isWrappedData}"

    hx-get="/translations/find"
    hx-target="#translation-form-container-${wordId}"
    hx-swap="innerHTML"
    hx-params="documentId,targetWord,selectedWordId,isPhrase,isFound"
    hx-vals='js:{
        documentId: ${documentId},
        targetWord: "${processedWord}",
        selectedWordId: ${wordId},
        isPhrase: false,
        isFound: data.get(htmx.find("#word-${wordId}"), "is-saved")
    }'
    hx-trigger="click"
    hx-on:click="

    documentPage.closeAllTranslationForms()
    documentPage.cleanupSelectedWord()
    documentPage.cleanupSelectedPhrase()

    util.findAllByData({ key: 'is-word', value: 'true' })
    .filter(word => data.get(word, 'is-selected') === 'false')
    .filter(word => data.get(word, 'is-selectable') === 'true')
    .filter(word => data.get(word, 'id') === '${wordId}')
    .flatMap(word => word.firstElementChild.nextElementSibling)
    .forEach(span => {
        data.set(span.parentElement, { key: 'is-selected', value: 'true' })
        util.replaceClasses(span, {
            toRemove: ['bg-accent', 'bg-accent/80', 'bg-accent/60', 'bg-accent/40', 'bg-primary', 'text-primary', 'text-accent', 'bg-tertiary'],
            toAdd: ['bg-tertiary', 'text-accent']
        })
    })
    ">
        <div
        onclick="event.stopPropagation()"
        class="
        cursor-default z-2 absolute inline left-2 top-10"
        id="translation-form-container-${wordId}"

        data-id="${wordId}"
        data-is-translation-form-container="true"

        hx-on::after-swap="htmx.find('form-position-adjuster').updatePositions(${wordId})">
        </div>
        <span
        class="${bgColor} ${textColor} rounded"
        id="word-span-${wordId}"

        data-id="${wordId}"
        data-value="${processedWord}"
        data-familiarity="${familiarityString}"
        data-is-selected="false"
        data-is-word-span="true">${word}</span>
    </div>
@else
    <div
    class="
    inline-block relative rounded whitespace-nowrap text-lg pointer-events-none select-none"

    data-familiarity="${familiarityString}"
    data-is-word="true"
    data-is-selectable="false">
        <span
        class="${bgColor} ${textColor} rounded">${word}</span>
    </div>
@endif
