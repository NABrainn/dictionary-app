@import lule.dictionary.controllerAdvice.data.BaseAttribute
@import lule.dictionary.translations.data.Familiarity
@import lule.dictionary.translations.data.attribute.TranslationAttribute
@import java.util.List
@import java.util.Map

@param BaseAttribute baseAttribute 
@param int selectableId
@param int documentId
@param int phraseLength
@param List<String> familiarities
@param List<String> isSavedList
@param TranslationAttribute attribute
@param Map<String, String> errors

!{var cleanWordPattern = java.util.regex.Pattern.compile("[^\\p{L}\\p{N}\\s-]");}
!{String[] unprocessedPhraseText = attribute.translation().targetWord().split(" ");}
!{String[] processedPhraseText = cleanWordPattern
    .matcher(attribute.translation().targetWord())
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

hx-params="documentId,targetWord,selectedWordId,isPhrase,isPersisted"
hx-get="/translations/find"
hx-target="#translation-form-container-${selectableId}"
hx-swap="innerHTML"
hx-vals='{
    "documentId": "${documentId}",
    "targetWord": "${String.join(" ", processedPhraseText)}",
    "selectedWordId": "${selectableId}",
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
        @template.translation.add-translation-form(
        attribute = attribute,
        baseAttribute = baseAttribute,
        errors = errors)
    </div>
    @for(int wordId = selectableId, phraseWordId = 0; wordId < selectableId + phraseLength; wordId++, phraseWordId++)
        @template.document.word(
        wordId = wordId,
        documentId = documentId,
        word = unprocessedPhraseText[phraseWordId],
        processedWord = processedPhraseText[phraseWordId],
        familiarity = phraseWordFamiliarityList.get(phraseWordId),
        isPersisted = phraseWordIsSavedList.get(phraseWordId),
        isWrapped = true)
    @endfor
</div>