@import lule.dictionary.dto.application.BaseAttribute
@import lule.dictionary.dto.application.attribute.NavbarAttribute
@import lule.dictionary.service.imports.importService.dto.Word
@import lule.dictionary.service.imports.importService.dto.Phrase
@import lule.dictionary.service.imports.importService.dto.request.DocumentAttribute
@import gg.jte.support.ForSupport
@import java.util.regex.Pattern

@param NavbarAttribute navbarAttribute
@param DocumentAttribute documentAttribute
@param BaseAttribute baseAttribute

!{var translations = documentAttribute.importData().translations();}
!{var content = documentAttribute.importData().content();}
!{var documentId = documentAttribute.importData().documentId();}

<form
id="page-content"
class="flex flex-col gap-8 text-wrap"

data-document-id="${documentAttribute.importData().documentId()}"
hx-trigger="createphrase from:document"
hx-get="/translations/create-phrase"
hx-target="#new-phrase"
hx-swap="outerHTML"
hx-vals='js:{
    selectableId: data.get(htmx.find("#new-phrase"), "id"),
    phraseText: data.get(htmx.find("#new-phrase"), "value"),
    documentId: ${documentId},
    phraseLength: data.get(htmx.find("#new-phrase"), "length"),
    familiarities: util.findAllByData({ key: "is-wrapped", value: "true" }).map(word => data.get(word, "familiarity")),
    isSavedList: util.findAllByData({ key: "is-wrapped", value: "true" }).map(word => data.get(word, "is-saved") ?? "false"),
}'>
    @template.navbar.navbar(
    navbarAttribute = navbarAttribute,
    isAuthenticated = baseAttribute.isAuthenticated(),
    isProfileToggleBtnVisible = false)
    <div
    id="content-container"
    class="
    w-full px-2
    md:w-3/5 mx-auto text-wrap min-h-165"

    hx-on:mouseup="
    const familiarityColors = new Map([
        ['unknown', ['bg-accent', 'text-primary']],
        ['recognized', ['bg-accent/80', 'text-primary']],
        ['familiar', ['bg-accent/60', 'text-primary']],
        ['known', ['bg-primary', 'text-neutral']],
        ['ignored', ['bg-primary', 'text-neutral']]
    ])
    const focusId = parseInt(window.getSelection()?.focusNode?.parentElement?.parentElement?.dataset?.id);
    const anchorId = parseInt(window.getSelection()?.anchorNode?.parentElement?.parentElement?.dataset?.id);
    if(focusId === anchorId) {
        return
    }
    const phraseNodes =  Array.from(
        { length: focusId - anchorId + 1 },
        (_, i) => htmx.find('#word-' + (i + anchorId))
    )
    const result = util.catchErr(() => {
        if(!phraseNodes) {
            throw new Error('phrase nodes cant be undefined')
        }
        if(!phraseNodes.length) {
            throw new Error('phrase nodes cant be empty')
        }
        if(phraseNodes.length > 5) {
            throw new Error('selection too long')
        }
        if(phraseNodes.some(node => data.get(node, 'is-selected') === 'true')) {
            throw new Error('cannot create phrase from selected word')
        }
        if(phraseNodes.some(node => data.get(node, 'is-wrapped') === 'true')) {
            throw new Error('cannot create phrase from wrapped word')
        }
        if(!phraseNodes.every(node => data.get(node, 'is-selectable') === 'true')) {
            throw new Error('all phrase parent nodes must be selectable')
        }
    })
    if(result.err) {
        return window.getSelection().removeAllRanges()
    }
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

    util.findAllByData({ key: 'is-translation-form-container', value: 'true' })
    .forEach(formContainer => util.removeInnerHTML(formContainer))

    const phraseText = phraseNodes
    .flatMap(node => [...node.children])
    .filter(child => child instanceof HTMLSpanElement)
    .map(span => span.innerText)
    .join(' ')
    const phraseId = data.get(phraseNodes.at(0), 'id')
    const wrappedPhrase = util.wrap({
        wrapper: util.define('div', {
            classList: ['inline-flex', 'relative', 'rounded', 'text-lg', 'gap-1', 'cursor-pointer', 'border', 'border-2', 'border-secondary'],
            data: [
                { key: 'id', value: phraseId },
                { key: 'value', value: phraseText },
                { key: 'is-phrase', value: 'true' },
                { key: 'is-saved', value: 'false' },
                { key: 'is-selected', value: 'true' },
                { key: 'length', value: phraseNodes.length }
            ],
            id: 'new-phrase'
        }),
        content: phraseNodes
    })

    htmx.on('htmx:afterSettle', (e) => {
        const adjuster = htmx.find('form-position-adjuster')
        adjuster.updateFormPosition(e.target.firstElementChild)
    })



    wrappedPhrase.content.forEach(node => node.classList.remove('border', 'border-2', 'border-transparent'))
    util.findAllByData({ key: 'is-translation-form-container', value: 'true' })
    .forEach(formContainer => util.removeInnerHTML(formContainer))

    document.dispatchEvent(new Event('createphrase'))
    ">
        @if(documentAttribute.paginationData().currentPageNumber() == 1)
            <span
            id="content-title"
            class="
            md:text-4xl
            text-xl text-neutral font-bold ">${documentAttribute.importData().title()}</span>
        @endif
        <div
        id="content-main"
        class="leading-9 flex flex-col">!{var cleanWordPattern = Pattern.compile("[^\\p{L}\\p{N}\\s-]");}
            @for(var paragraphEntry :  ForSupport.of(content.paragraphs()))
                !{var paragraph = paragraphEntry.get(); var paragraphId = paragraphEntry.getIndex();}
                <div
                class="paragraph-${paragraphId} inline-flex flex-wrap"
                data-paragraph-id="${paragraphId}">
                    @for(var selectableEntry : ForSupport.of(paragraphEntry.get()))!{var selectable = selectableEntry.get();}
                        !{var selectableId = selectableEntry.getIndex();}@if(selectable instanceof Word)
                        !{var wordId = content.startIndices().get(paragraphId) + selectableId;}
                        !{var word = ((Word) selectable).value();}
                        !{var cleanWord = cleanWordPattern.matcher(word).replaceAll("")
                                                .replace("-", "")
                                                .toLowerCase();}
                        @if(!cleanWord.isEmpty())
                            @if(translations.get(cleanWord) != null)
                                !{String unselectedBgColor = switch(translations.get(cleanWord).familiarity()){
                                    case UNKNOWN -> "bg-accent";
                                    case RECOGNIZED -> "bg-accent/80";
                                    case FAMILIAR -> "bg-accent/60";
                                    case KNOWN -> "bg-primary";
                                    default -> "bg-primary";
                                };}
                                !{String unselectedTextColor = switch(translations.get(cleanWord).familiarity()){
                                    case UNKNOWN, RECOGNIZED, FAMILIAR -> "text-primary";
                                    default -> "text-neutral";
                                };}
                                @template.document-page.content.word(
                                wordId = wordId,
                                processedWord = cleanWord,
                                documentId = documentId,
                                word = word,
                                bgColor = unselectedBgColor,
                                textColor = unselectedTextColor,
                                familiarity = translations.get(cleanWord).familiarity(),
                                isFound = true)
                            @else
                                @template.document-page.content.word(
                                    wordId = wordId,
                                    processedWord = cleanWord,
                                    documentId = documentId,
                                    word = word,
                                    bgColor = "bg-accent",
                                    textColor = "text-primary"
                                )
                            @endif
                        @else
                            @template.document-page.content.word(
                                wordId = wordId,
                                processedWord = cleanWord,
                                documentId = documentId,
                                word = word,
                                bgColor = "bg-primary",
                                textColor = "text-neutral",
                                isSelectable = false
                            )
                        @endif
                    @else
                        !{var phrase = ((Phrase) selectable);}
                        @template.document-page.content.update-phrase(
                        phrase = phrase,
                        cleanWordPattern = cleanWordPattern,
                        paragraphId = paragraphId,
                        selectableId = content.startIndices().get(paragraphId) + selectableId,
                        documentId = documentId,
                        startIndices = content.startIndices(),
                        translations = translations
                        )
                    @endif
                @endfor
                </div>
            @endfor
        </div>
    </div>
    <div
    class="
    md:max-w-2/5 md:gap-4
    max-w-full flex justify-center gap-2  mx-auto items-center">
        @template.document-page.content.paginator(
            id = documentAttribute.importData().documentId(),
            currentPage = documentAttribute.paginationData().currentPageNumber(),
            pages = documentAttribute.paginationData().numberOfPages(),
            importId = documentAttribute.importData().documentId(),
            rows = documentAttribute.paginationData().rows(),
            currentRow = documentAttribute.paginationData().currentRowNumber(),
            firstPageOfRow = documentAttribute.paginationData().firstPageOfRowNumber()
        )
    </div>
</form>