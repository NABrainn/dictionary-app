@import lule.dictionary.enumeration.Familiarity
@import lule.dictionary.service.translation.dto.attribute.TranslationAttribute
@import java.util.List
@import java.util.Map

@param int selectableId
@param int documentId
@param int phraseLength
@param List<String> familiarities
@param List<String> isSavedList
@param TranslationAttribute translationAttribute
@param Map<String, String> translationLocalization

!{var cleanWordPattern = java.util.regex.Pattern.compile("[^\\p{L}\\p{N}\\s-]");}
!{String[] unprocessedPhraseText = translationAttribute.translation().targetWord().split(" ");}
!{String[] processedPhraseText = cleanWordPattern
    .matcher(translationAttribute.translation().targetWord())
    .replaceAll("")
    .toLowerCase()
    .split(" ");}
!{List<Familiarity> phraseWordFamiliarityList = familiarities.stream()
    .map(String::toUpperCase)
    .map(familiarity -> switch (familiarity) {
        case "UNKNOWN" -> Familiarity.UNKNOWN;
        case "RECOGNIZED" -> Familiarity.RECOGNIZED;
        case "FAMILIAR" -> Familiarity.FAMILIAR;
        case "KNOWN" -> Familiarity.KNOWN;
        default -> Familiarity.IGNORED;
    })
    .toList();}
!{List<Boolean> phraseWordIsSavedList = isSavedList.stream()
    .map(bool -> switch (bool) {
        case "true" -> Boolean.TRUE;
        case "false" -> Boolean.FALSE;
        default -> throw new RuntimeException("not implemented");
    })
    .toList();}

<div
class="inline-flex relative rounded text-lg gap-1 cursor-pointer border border-2 border-secondary bg-tertiary text-accent"
id="phrase-${selectableId}"

data-id="${selectableId}"
data-value="${String.join(" ", processedPhraseText)}"
data-familiarity="unknown"

data-is-phrase="true"
data-is-saved="false"
data-is-selectable="true"
data-is-selected="true"

hx-params="documentId,targetWord,selectedWordId,isPhrase,isFound"
hx-get="/translations/find"
hx-target="#translation-form-container-${selectableId}"
hx-swap="innerHTML"
hx-vals='{
    "documentId": "${documentId}",
    "targetWord": "${String.join(" ", processedPhraseText)}",
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

//cleanup selected word
util.findAllByData({ key: 'is-word', value: 'true' })
.filter(word => data.get(word, 'is-selected') === 'true')
.flatMap(selectedWord => [...selectedWord.children])
.filter(child => child instanceof HTMLSpanElement)
.forEach(selectedSpan => {
    data.set(selectedSpan.parentElement, { key: 'is-selected', value: 'false' })
    util.replaceClasses(selectedSpan, {
        toRemove: ['bg-accent', 'bg-accent/80', 'bg-accent/60', 'bg-accent/40', 'bg-primary', 'text-primary', 'text-accent', 'bg-tertiary'],
        toAdd: familiarityColors.get(data.get(selectedSpan.parentElement, 'familiarity'))
    })
})
//cleanup selected phrase
util.findAllByData({ key: 'is-phrase', value: 'true' })
.filter(node => data.get(node, 'is-selected') === 'true')
.forEach(selectedPhrase => {
    data.set(selectedPhrase, { key: 'is-selected', value: 'false' })

    if(data.get(selectedPhrase, 'is-saved') === 'true') {
        util.replaceClasses(selectedPhrase, {
            toRemove: ['bg-accent', 'bg-accent/80', 'bg-accent/60', 'bg-accent/40', 'bg-primary', 'text-primary', 'text-accent', 'bg-tertiary'],
            toAdd: [...familiarityColors.get(data.get(selectedPhrase, 'familiarity'))]
        })
        const words = Array.from(selectedPhrase.children)
        .filter(child => data.get(child, 'is-word') === 'true')
        words.forEach(word => word.classList.add('pointer-events-none'))

        const spans = words
        .flatMap(word => [...word.children])
        .filter(word => word instanceof HTMLSpanElement)
        spans.forEach(span => {
            util.replaceClasses(span, {
                toRemove: ['text-accent', 'text-primary'],
                toAdd: [familiarityColors.get(data.get(selectedPhrase, 'familiarity')).at(1)]
            })
        })
    }
    else {
        selectedPhrase.firstElementChild.remove()
        const phraseNodes = util.unwrap(selectedPhrase).filter(node => data.get(node, 'is-word'))
        phraseNodes.forEach(node => {
            htmx.process(node)
            util.replaceClasses(node, {
                toRemove: ['pointer-events-none'],
                toAdd: ['border', 'border-2', 'border-transparent']
            })
            const span = [...node.children].filter(child => child instanceof HTMLSpanElement).at(0)
            util.replaceClasses(span, {
                toRemove: ['bg-accent', 'bg-accent/80', 'bg-accent/60', 'bg-accent/40', 'bg-primary', 'text-primary', 'text-accent', 'bg-tertiary', 'bg-tertiary'],
                toAdd: familiarityColors.get(data.get(node, 'familiarity'))
            })
        })
    }
})

//handle selection
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
    id="translation-form-container-${selectableId}"

    data-id="${selectableId}"
    data-is-translation-form-container="true">
        @template.document-page.content.translation.add.add-translation-form(
        translationAttribute = translationAttribute,
        translationLocalization = translationLocalization)
    </div>
    @for(int wordId = selectableId, phraseWordId = 0; wordId < selectableId + phraseLength; wordId++, phraseWordId++)
        @template.document-page.content.word(
        wordId = wordId,
        documentId = documentId,
        word = unprocessedPhraseText[phraseWordId],
        processedWord = processedPhraseText[phraseWordId],
        familiarity = phraseWordFamiliarityList.get(phraseWordId),
        isFound = phraseWordIsSavedList.get(phraseWordId),
        isWrapped = true)
    @endfor
</div>