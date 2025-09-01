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
    util.findAllByData({ key: 'is-word', value: 'true' })
    .filter(word => data.get(word, 'is-selected') === 'false')
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
