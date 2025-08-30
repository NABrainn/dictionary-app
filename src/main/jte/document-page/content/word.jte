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
!{String borderStyles = isWrapped ? "" : "border border-2  border-transparent";}
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

    data-is-selectable="true"
    data-is-selected="false"
    data-is-word="true"

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
    const familiarityColors = new Map([
        ['unknown', ['bg-accent', 'text-primary']],
        ['recognized', ['bg-accent/80', 'text-primary']],
        ['familiar', ['bg-accent/60', 'text-primary']],
        ['known', ['bg-primary', 'text-neutral']],
        ['ignored', ['bg-primary', 'text-neutral']]
    ])

    util.findAllByData({ key: 'is-translation-form-container', value: 'true' })
    .forEach(formContainer => util.removeInnerHTML(formContainer))

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
        data-is-selected="false">${word}</span>
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
