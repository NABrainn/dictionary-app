@import lule.dictionary.translations.data.Familiarity

@param int wordId
@param int documentId
@param String word
@param String processedWord
@param String bgColor = ""
@param String textColor = ""
@param boolean isSelectable = true
@param boolean isPhraseWord = false
@param Familiarity familiarity
@param boolean isPersisted = false
@param boolean isWrapped = false
@if(isSelectable)
    <div
    class="
    ${isWrapped ? "pointer-events-none" : "border border-2  border-transparent"} inline-block rounded-none relative cursor-pointer rounded whitespace-nowrap text-lg"
    id="word-${wordId}"

    data-id="${wordId}"
    data-value="${processedWord}"
    data-familiarity="${familiarity != null ? familiarity.name().toLowerCase() : "unknown"}"

    data-is-selectable="true"
    data-is-selected="false"
    data-is-word="true"
    data-is-saved="${isPersisted  ? "true" : "false"}"
    data-is-wrapped="${isWrapped  ? "true" : "false"}"

    hx-get="/translations/find"
    hx-target="#translation-form-container-${wordId}"
    hx-swap="innerHTML"
    hx-params="documentId,targetWord,selectedWordId,isPersisted"
    hx-vals='js:{
        documentId: ${documentId},
        targetWord: "${processedWord}",
        selectedWordId: ${wordId},
        isPersisted: data.get(htmx.find("#word-${wordId}"), "is-saved")
    }'
    hx-on:click="
    documentPage.closeAllTranslationForms()
    documentPage.cleanupSelectedWord()
    documentPage.cleanupSelectedPhrase()
    documentPage.handleWordClick(${wordId})">
        <div
        class="
        cursor-default z-2 absolute inline"
        id="translation-form-container-${wordId}"
        data-is-translation-form-container="true"
        hx-on::after-swap="formAdjuster.updatePositions(${wordId})"></div>
        <span
        class="${bgColor} ${textColor} rounded"
        data-is-word-span="true">${word}</span>
    </div>
@else
    <div
    class="
    inline-block relative rounded whitespace-nowrap text-lg pointer-events-none select-none">
        <span class="${bgColor} ${textColor} rounded">${word}</span>
    </div>
@endif