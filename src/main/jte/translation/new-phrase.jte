@import lule.dictionary.controllerAdvice.data.BaseAttribute
@import lule.dictionary.translations.data.TranslationLocalizationKey
@import lule.dictionary.translations.data.attribute.PhraseAttribute
@import java.util.Map

@param BaseAttribute baseAttribute
@param PhraseAttribute attribute
@param Map<TranslationLocalizationKey, String> messages
@param Map<String, String> errors

<div
class="inline-flex relative rounded text-lg gap-1 cursor-pointer border border-2 border-secondary bg-tertiary text-accent"
id="phrase-${attribute.phraseAttribute().id()}"

data-id="${attribute.phraseAttribute().id()}"
data-value="${attribute.phraseAttribute().translation().targetWord().toLowerCase()}"
data-familiarity="unknown"

data-is-phrase="true"
data-is-saved="false"
data-is-selectable="true"
data-is-selected="true"

hx-params="documentId,targetWord,id,isPhrase,isPersisted"
hx-get="/translations/find"
hx-target="#translation-form-container-${attribute.phraseAttribute().id()}"
hx-swap="innerHTML"
hx-vals='{
    "documentId": "${attribute.phraseAttribute().documentId()}",
    "targetWord": "${attribute.phraseAttribute().translation().targetWord()}",
    "id": "${attribute.phraseAttribute().id()}",
    "isPhrase": "true",
    "isPersisted": "true"
}'
hx-trigger="click"
hx-on::before-request="
documentPage.closeAllTranslationForms()
documentPage.cleanupSelectedWord()
documentPage.cleanupSelectedPhrase()

//handle selection
util.findAllByData({ key: 'is-phrase', value: 'true' })
.filter(node => data.get(node, 'is-selected') === 'false')
.filter(node => data.get(node, 'id') === '${attribute.phraseAttribute().id()}')
.forEach(node => {
    data.set(node, { key: 'is-selected', value: 'true' })
    util.replaceClasses(node, {
        toRemove: ['bg-accent', 'bg-accent/80', 'bg-accent/60', 'bg-accent/40', 'bg-primary', 'text-primary'],
        toAdd: ['bg-tertiary', 'text-accent']
    })
    Array.from(node.children)
    .filter(child => data.get(child, 'is-word') === 'true')
    .flatMap(word => [...word.children])
    .filter(child => child instanceof HTMLSpanElement)
    .forEach(span => {
        util.replaceClasses(span, {
            toRemove: ['text-primary'],
            toAdd: ['text-accent']
        })
    })
})
">
    <div
    onclick="event.stopPropagation()"
    class="
    cursor-default z-2 absolute inline left-2 top-10"
    id="translation-form-container-${attribute.phraseAttribute().id()}"

    data-id="${attribute.phraseAttribute().id()}"
    data-is-translation-form-container="true">
        @template.translation.add-translation-form(
        attribute = attribute.phraseAttribute(),
        baseAttribute = baseAttribute,
        messages = messages,
        errors = errors)
    </div>
    @for(int wordId = 0; wordId < attribute.phrasePartsAttribute().size(); wordId++)
        @template.document.word(
        wordId = attribute.phrasePartsAttribute().get(wordId).id(),
        documentId = attribute.phraseAttribute().documentId(),
        word = attribute.phrasePartsAttribute().get(wordId).translation().unprocessedTargetWord(),
        processedWord = attribute.phrasePartsAttribute().get(wordId).translation().targetWord(),
        familiarity = attribute.phrasePartsAttribute().get(wordId).translation().familiarity(),
        isPersisted = attribute.phrasePartsAttribute().get(wordId).isPersisted(),
        isWrapped = true)
    @endfor
</div>