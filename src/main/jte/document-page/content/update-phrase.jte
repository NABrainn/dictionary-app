@import gg.jte.support.ForSupport
@import lule.dictionary.dto.database.interfaces.translation.Translation
@import lule.dictionary.enumeration.Familiarity
@import lule.dictionary.service.imports.importService.dto.Phrase
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
hx-vals='{
    "documentId": "${documentId}",
    "targetWord": "${processedWord}",
    "selectedWordId": "${selectableId}",
    "isPhrase": "true",
    "isFound": "true"
}'
hx-trigger="click"
hx-on:click="
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
    hx-on::after-swap="htmx.find('form-position-adjuster').updatePositions(${selectableId})">
    </div>
    @for(var phraseEntry : ForSupport.of(phrase.targetWords()))
        !{var wordId = 0;}
        @if(phrase.targetWords().size() == 1)
            !{wordId = startIndices.get(paragraphId) + (phraseEntry.getIndex());}
        @else
            !{wordId = startIndices.get(paragraphId) + (phraseEntry.getIndex() - 1);}
        @endif
        !{var word = phraseEntry.get();}
        !{var cleanWord = cleanWordPattern.matcher(word)
                                                .replaceAll("")
                                                .replace("-", " ")
                                                .toLowerCase();}
        @if(!cleanWord.isEmpty())
            @if(translations.get(cleanWord) != null)
                @template.document-page.content.word(
                wordId = wordId,
                processedWord = cleanWord,
                documentId = documentId,
                word = word,
                familiarity = translations.get(cleanWord).familiarity(),
                isSelectable = false,
                isWrapped = true,
                phraseFamiliarity = phrase.familiarity())
            @else
                @template.document-page.content.word(
                wordId = wordId,
                processedWord = cleanWord,
                documentId = documentId,
                word = word,
                isSelectable = false,
                isWrapped = true,
                phraseFamiliarity = phrase.familiarity())
            @endif
        @else
            @template.document-page.content.word(
                wordId = wordId,
                processedWord = cleanWord,
                documentId = documentId,
                word = word,
                textColor = containerTextColor,
                isSelectable = false,
            isWrapped = true,
            phraseFamiliarity = phrase.familiarity()
            )
        @endif
    @endfor
</div>
