@param int selectableId
@param String phraseText
@param int documentId
@param int phraseLength

!{var cleanWordPattern = java.util.regex.Pattern.compile("[^\\p{L}\\p{N}\\s-]");}
!{String[] unprocessedPhraseText = phraseText.split(" ");}
!{String[] processedPhraseText = cleanWordPattern
    .matcher(phraseText)
    .replaceAll("")
    .split(" ");}

<div
class="pointer-events-none inline-flex relative rounded text-lg gap-1 cursor-pointer border border-2 border-secondary bg-tertiary text-accent"
id="phrase-${selectableId}"

data-id="${selectableId}"
data-value="${phraseText}"

data-is-phrase="true"
data-is-saved="true"
data-is-selectable="true"
data-is-selected="false"

hx-get="/translations/find"
hx-target="#translation-form-container-${selectableId}"
hx-swap="innerHTML"
hx-vals='{
    "documentId": "${documentId}",
    "targetWord": "${phraseText}",
    "selectedWordId": "${selectableId}",
    "isPhrase": "true",
    "isFound": "true"
}'
hx-trigger="click"
hx-on::before-request="
const familiarityColors = new Map([
    ['unknown', ['bg-accent', 'text-primary']],
    ['recognized', ['bg-accent/80', 'text-primary']],
    ['familiar', ['bg-accent/60', 'text-primary']],
    ['known', ['bg-primary', 'text-neutral']],
    ['ignored', ['bg-primary', 'text-neutral']]
])

util.findAllByData({ key: 'is-translation-form-container', value: 'true' })
.forEach(formContainer => util.removeInnerHTML(formContainer))

util.findAllByData({ key: 'is-word', value: 'true' })
.filter(word => data.get(word, 'is-selected') === 'true')
.flatMap(selectedWord => selectedWord.firstElementChild.nextElementSibling)
.forEach(selectedSpan => {
    data.set(selectedSpan.parentElement, { key: 'is-selected', value: 'false' })
    util.replaceClasses(selectedSpan, {
        toRemove: ['bg-accent', 'bg-accent/80', 'bg-accent/60', 'bg-accent/40', 'bg-primary', 'text-primary', 'text-accent', 'bg-tertiary'],
        toAdd: familiarityColors.get(data.get(selectedSpan.parentElement, 'familiarity'))
    })
})

util.findAllByData({ key: 'is-phrase', value: 'true' })
.filter(node => data.get(node, 'is-selected') === 'true')
.forEach(selectedPhrase => {
    data.set(selectedPhrase, { key: 'is-selected', value: 'false' })
    util.replaceClasses(selectedPhrase, {
        toRemove: ['bg-accent', 'bg-accent/80', 'bg-accent/60', 'bg-accent/40', 'bg-primary', 'text-primary', 'text-accent', 'bg-tertiary'],
        toAdd: familiarityColors.get(data.get(selectedPhrase, 'familiarity'))
    })
    Array.from(selectedPhrase.children)
    .filter(child => data.get(child, 'is-word') === 'true')
    .flatMap(word => word.firstElementChild)
    .forEach(span => {
        util.replaceClasses(span, {
            toRemove: ['text-accent', 'text-primary'],
            toAdd: [familiarityColors.get(data.get(selectedPhrase, 'familiarity')).at(1)]
        })
    })
})

util.findAllByData({ key: 'is-phrase', value: 'true' })
.filter(node => data.get(node, 'is-selected') === 'false')
.filter(node => data.get(node, 'id') === '${selectableId}')
.forEach(node => {
    data.set(node, { key: 'is-selected', value: 'true' })
    util.replaceClasses(node, {
        toRemove: ['bg-accent', 'bg-accent/80', 'bg-accent/60', 'bg-accent/40', 'bg-primary', 'text-primary'],
        toAdd: ['bg-tertiary', 'text-accent']
    })
    Array.from(node.children)
    .filter(child => data.get(child, 'is-word') === 'true')
    .flatMap(word => word.firstElementChild)
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
    id="translation-form-container-${selectableId}"

    data-id="${selectableId}"
    data-is-translation-form-container="true"
    hx-on::after-swap="
    htmx.find('form-position-adjuster').updatePositions(${selectableId})
    ">
    </div>
    @for(int wordId = selectableId, phraseWordId = 0; wordId < selectableId + phraseLength; wordId++, phraseWordId++)
        @template.document-page.content.word(
        wordId = wordId,
        documentId = documentId,
        word = unprocessedPhraseText[phraseWordId],
        processedWord = processedPhraseText[phraseWordId],
        isWrapped = true)
    @endfor
</div>