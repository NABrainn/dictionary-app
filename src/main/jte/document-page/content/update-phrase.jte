@import gg.jte.support.ForSupport
@import lule.dictionary.translations.data.Translation
@import lule.dictionary.translations.data.Familiarity
@import lule.dictionary.documents.data.selectable.Phrase
@import java.util.List
@import java.util.Map
@import java.util.regex.Pattern

@param Phrase phrase
@param Pattern cleanWordPattern
@param int paragraphId
@param int selectableId
@param int documentId
@param List<Integer> startIndices
@param Map<String, Translation> translations

!{var processedWord = String.join(" ", phrase.targetWords().stream()
                                                .map(word -> cleanWordPattern.matcher(word)
                                                        .replaceAll("")
                                                        .replace("-", " ")
                                                        .toLowerCase())
                                                .toList());}

!{var containerBgColor = "";}
!{var containerTextColor = "";}
!{var familiarity = "";}

@if(phrase.familiarity() != null)
    @if(phrase.familiarity() == Familiarity.UNKNOWN)
        !{containerBgColor = "bg-accent";}
        !{containerTextColor = "text-primary";}
    @elseif(phrase.familiarity() == Familiarity.RECOGNIZED)
        !{containerBgColor = "bg-accent/80";}
        !{containerTextColor = "text-primary";}
    @elseif(phrase.familiarity() == Familiarity.FAMILIAR)
        !{containerBgColor = "bg-accent/60";}
        !{containerTextColor = "text-primary";}
    @elseif(phrase.familiarity() == Familiarity.KNOWN)
        !{containerBgColor = "bg-primary";}
        !{containerTextColor = "text-neutral";}
    @else
        !{containerBgColor = "bg-primary";}
        !{containerTextColor = "text-neutral";}
    @endif
    !{familiarity = phrase.familiarity().name().toLowerCase();}
@else
    !{containerBgColor = "bg-primary";}
    !{containerTextColor = "text-neutral";}
@endif
<div
class="inline-flex relative rounded text-lg gap-1 cursor-pointer border border-2 border-secondary ${containerTextColor} ${containerBgColor}"
id="phrase-${selectableId}"

data-id="${selectableId}"
data-value="${processedWord}"
data-familiarity="${familiarity}"

data-is-phrase="true"
data-is-saved="true"
data-is-selectable="true"
data-is-selected="false"

hx-get="/translations/find"
hx-target="#translation-form-container-${selectableId}"
hx-swap="innerHTML"
hx-params="documentId,targetWord,selectedWordId,isPhrase,isFound"
hx-vals='{
    "documentId": "${documentId}",
    "targetWord": "${processedWord}",
    "selectedWordId": "${selectableId}",
    "isPhrase": "true",
    "isFound": "true"
}'
hx-trigger="click"
hx-on:click="
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
            toRemove: ['text-primary', 'text-accent'],
            toAdd: ['text-accent']
        })
    })
})
">
    <div
    onclick="event.stopPropagation()"
    class="
    cursor-default z-2 absolute inline"
    id="translation-form-container-${selectableId}"

    data-id="${selectableId}"
    data-is-translation-form-container="true"
    hx-on::after-swap="formAdjuster.updatePositions(${selectableId})">
    </div>
    @for(var phraseEntry : ForSupport.of(phrase.targetWords()))
        !{var word = phraseEntry.get();}
        !{var cleanWord = cleanWordPattern.matcher(word)
                                                .replaceAll("")
                                                .replace("-", " ")
                                                .toLowerCase();}
        @if(!cleanWord.isEmpty())
            @if(translations.get(cleanWord) != null)
                @template.document-page.content.word(
                wordId = -1,
                processedWord = cleanWord,
                documentId = documentId,
                word = word,
                familiarity = translations.get(cleanWord).familiarity(),
                isSelectable = false,
                isWrapped = true,
                phraseFamiliarity = phrase.familiarity())
            @else
                @template.document-page.content.word(
                wordId = -1,
                processedWord = cleanWord,
                documentId = documentId,
                word = word,
                isSelectable = true,
                isWrapped = true,
                phraseFamiliarity = phrase.familiarity())
            @endif
        @else
            @template.document-page.content.word(
            wordId = -1,
            processedWord = cleanWord,
            documentId = documentId,
            word = word,
            textColor = containerTextColor,
            isSelectable = false,
            isWrapped = true,
            phraseFamiliarity = phrase.familiarity())
        @endif
    @endfor
</div>
