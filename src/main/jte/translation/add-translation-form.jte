@import lule.dictionary.controllerAdvice.data.BaseAttribute
@import lule.dictionary.translations.data.TranslationLocalizationKey
@import lule.dictionary.translations.data.attribute.TranslationAttribute
@import java.util.Map

@param BaseAttribute baseAttribute 
@param TranslationAttribute attribute
@param Map<String, String> errors
@param Map<TranslationLocalizationKey, String> messages

!{var processedWord = attribute.translation().targetWord().replaceAll("[^\\p{L}\\p{N}\\s-]", "").toLowerCase();}

<div
onclick="event.stopPropagation()"
class="
md:gap-0.75
bg-tertiary flex flex-col p-3 rounded pointer-events-auto animate-fade"
id="translation-form">
    <div class="flex justify-end">
        <span
        class="md:text-md
        text-sm text-red-400 font-bold cursor-pointer"
        hx-on:click="documentPage.closeAllTranslationForms()">❌</span>
    </div>
    <span class="font-bold text-lg text-accent text-wrap">${processedWord}</span>
    <div id="translation-manager">
        <div class="flex flex-col gap-2 text-wrap">
            @for(String word : attribute.translation().sourceWords())
                <div
                class="flex gap-2 items-center min-h-8">
                    <input
                    name="sourceWords"
                    type="hidden"
                    value="${word}">
                    <span class="block text-center text-accent text-wrap">${word}</span>
                </div>
            @endfor
        </div>
        <div class="flex flex-col gap-2">
            <div id="source-word-form" class="flex">
                <form
                class="flex flex-col gap-2"
                hx-params="selectedWordId,targetWord,familiarity,sourceLanguage,targetLanguage,documentId,sourceWords,_csrf"
                hx-vals='
                {
                    "selectedWordId": "${attribute.selectedWordId()}",
                    "targetWord": "${attribute.translation().targetWord()}",
                    "familiarity": "UNKNOWN",
                    "sourceLanguage": "${attribute.translation().sourceLanguage()}",
                    "targetLanguage": "${attribute.translation().targetLanguage()}",
                    "documentId": "${attribute.documentId()}",
                    "_csrf": "${baseAttribute._csrf().getToken()}"
                }'
                hx-post="/translations/new"
                hx-target="#translation-form-container-${attribute.selectedWordId()}"
                hx-swap="innerHTML">
                    @for(String word : attribute.translation().sourceWords())
                        <input
                        name="sourceWords"
                        type="hidden"
                        value="${word}">
                    @endfor
                    <div class="flex gap-2">
                        <input
                        id="input-source-word"
                        class="
                        outline-none border border-2 ${errors.get("sourceWord") != null ? "border-error" : "border-primary"} rounded text-neutral text-md grow-1
                        focus:border-accent"
                        type="text"
                        placeholder=""
                        name="sourceWords">
                        <button
                        id="submit-source-word"
                        class="text-3xl cursor-pointer text-accent"
                        type="submit">+</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <div class="
    md:flex md:flex-col md:gap-0.25
    flex flex-row justify-around">
        @for(var level : attribute.familiarityLevels().entrySet())
            <form
            hx-on::before-request="
            const familiarityColors = new Map([
                ['unknown', ['bg-accent', 'text-primary']],
                ['recognized', ['bg-accent/80', 'text-primary']],
                ['familiar', ['bg-accent/60', 'text-primary']],
                ['known', ['bg-primary', 'text-neutral']],
                ['ignored', ['bg-primary', 'text-neutral']]
            ])
            if('${processedWord}'.split(' ').length > 1) {
                util.findAllByData({ key: 'is-phrase', value: 'true' })
                .filter(phrase => data.get(phrase, 'is-saved') === 'false')
                .filter(phrase => data.get(phrase, 'is-selected') === 'true')
                .forEach(selectedPhrase => {
                    data.set(selectedPhrase, { key: 'familiarity', value: '${level.getValue().name().toLowerCase()}' })
                    data.set(selectedPhrase, { key: 'is-saved', value: 'true' })
                    util.replaceClasses(selectedPhrase, {
                        toRemove: ['bg-accent', 'bg-accent/80', 'bg-accent/60', 'bg-accent/40', 'bg-primary', 'text-primary'],
                        toAdd: ['bg-tertiary', 'text-accent']
                    })
                    const words = Array.from(selectedPhrase.children)
                    .filter(child => data.get(child, 'is-word') === 'true')
                    words.forEach(word => word.classList.add('pointer-events-none'))

                    const spans = words.flatMap(word => word.firstElementChild)
                    spans.forEach(span => {
                        util.replaceClasses(span, {
                            toRemove: ['bg-accent', 'bg-accent/80', 'bg-accent/60', 'bg-accent/40', 'bg-primary', 'text-primary', 'text-accent', 'text-primary'],
                            toAdd: ['text-accent']
                        })
                    })
                })

                //highlight phrases matched by text
                const matchedPhraseNodes = util.findAllByText({ text: '${processedWord}', nodes: util.findAllByData({ key: 'is-word-span', value: 'true' }) })
                .map(spanNodeList => spanNodeList.map(spanNode => spanNode.parentElement))
                .filter(wordList => wordList.every(word => data.get(word, 'is-wrapped') === 'false'))
                matchedPhraseNodes
                .forEach(phraseNodeList => {
                    const wrapped = util.wrap({
                        wrapper: util.define('div', {
                            classList: ['inline-flex', 'relative', 'rounded', 'text-lg', 'gap-1', 'cursor-pointer', 'border', 'border-2', 'border-secondary'],
                            data: [
                                { key: 'id', value: data.get(phraseNodeList.at(0), 'id') },
                                { key: 'value', value: '${processedWord}' },
                                { key: 'is-phrase', value: 'true' },
                                { key: 'is-saved', value: 'true' },
                                { key: 'is-selected', value: 'false' },
                                { key: 'length', value: '${processedWord}'.split(' ').length }
                            ],
                        }),
                        content: phraseNodeList
                    })

                    util.insertBefore({
                        insert: util.define('div', {
                            classList: ['cursor-default', 'z-2', 'absolute', 'inline', 'left-2', 'top-10'],
                            id: phraseNodeList.at(0).firstElementChild.id,
                            data: [
                                { key: 'is-translation-form-container', value: 'true' }
                            ]
                        }),
                        before: wrapped.content.at(0)
                    })
                    util.replaceClasses(wrapped.wrapper, {
                        toRemove: ['bg-accent', 'bg-accent/80', 'bg-accent/60', 'bg-accent/40', 'bg-primary', 'text-primary', 'text-accent'],
                        toAdd: familiarityColors.get('${level.getValue().name().toLowerCase()}')
                    })
                    wrapped.content.forEach(phraseNode => {
                        data.set(phraseNode, { key: 'is-wrapped', value: 'true' });
                        data.set(phraseNode, { key: 'is-selectable', value: 'false' });
                        util.replaceClasses(phraseNode, {
                            toRemove: ['border', 'border-2', 'border-transparent'],
                            toAdd: ['pointer-events-none', 'text-accent']
                        })
                        phraseNode.firstElementChild.remove()

                        const spans = [...phraseNode.children]
                        .filter(child => child instanceof HTMLSpanElement)

                        spans.forEach(span => util.replaceClasses(span, {
                            toRemove: ['bg-accent', 'bg-accent/80', 'bg-accent/60', 'bg-accent/40', 'bg-primary', 'text-primary', 'text-accent', 'bg-tertiary'],
                            toAdd: familiarityColors.get('${level.getValue().name().toLowerCase()}').at(1)
                        }))
                    })
                    util.setHx({
                        element: wrapped.wrapper,
                        method: { key: 'hx-get', value: '/translations/find' },
                        target: '#translation-form-container-' + data.get(phraseNodeList.at(0), 'id'),
                        swap: 'innerHTML',
                        trigger: 'click',
                        params: 'documentId,targetWord,selectedWordId,isPhrase,isPersisted',
                        vals: {
                            documentId: data.get(phraseNodeList.at(0), 'document-id'),
                            targetWord: '${processedWord}',
                            selectedWordId: data.get(phraseNodeList.at(0), 'id'),
                            isPhrase: true,
                            isPersisted: true
                        }
                    })

                    htmx.on(wrapped.wrapper, 'click', () => {
                        const familiarityColors = new Map([
                            ['unknown', ['bg-accent', 'text-primary']],
                            ['recognized', ['bg-accent/80', 'text-primary']],
                            ['familiar', ['bg-accent/60', 'text-primary']],
                            ['known', ['bg-primary', 'text-neutral']],
                            ['ignored', ['bg-primary', 'text-neutral']]
                        ])

                        documentPage.closeAllTranslationForms()
                        documentPage.cleanupSelectedWord()
                        documentPage.cleanupSelectedPhrase()

                        //handle selection
                        util.findAllByData({ key: 'is-phrase', value: 'true' })
                        .filter(node => data.get(node, 'is-selected') === 'false')
                        .filter(node => data.get(node, 'id') === data.get(phraseNodeList.at(0), 'id'))
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
                    })
                })
            }
            else {
                util.findAllByData({ key: 'is-word', value: 'true' })
                .filter(word => data.get(word, 'value') === '${processedWord}')
                .forEach(word => {
                    data.set(word, { key: 'familiarity', value: '${level.getValue().name().toLowerCase()}' })
                    data.set(word, { key: 'is-saved', value: 'true' })
                    if(data.get(word, 'is-selected') === 'true') {
                        const selectedSpan = word.firstElementChild.nextElementSibling
                        util.replaceClasses(selectedSpan, {
                            toRemove: ['bg-accent', 'bg-accent/80', 'bg-accent/60', 'bg-accent/40', 'bg-primary', 'text-primary'],
                            toAdd: ['bg-tertiary', 'text-accent']
                        })
                    }
                    else {
                        const selectedSpan = word.firstElementChild.nextElementSibling
                        util.replaceClasses(selectedSpan, {
                            toRemove: ['bg-accent', 'bg-accent/80', 'bg-accent/60', 'bg-accent/40', 'bg-primary', 'text-primary'],
                            toAdd: familiarityColors.get('${level.getValue().name().toLowerCase()}')
                        })
                    }
                })
            }"
            class="
            md:bg-tertiary md:text-accent md:size-auto md:gap-1 md:cursor-normal md:justify-start
            cursor-pointer bg-accent text-primary flex gap-4 hover:text-primary font-bold rounded size-8  justify-center items-center"
            id="translation-form-${attribute.selectedWordId()}"
            hx-trigger="click"
            hx-post="/translations/new"
            hx-params="targetWord,familiarity,sourceLanguage,targetLanguage,selectedWordId,isPhrase,sourceWords,documentId,_csrf"
            hx-vals='{
                "targetWord": "${processedWord}",
                "familiarity": "${level.getValue().name()}",
                "sourceLanguage": "${attribute.translation().sourceLanguage().name()}",
                "targetLanguage": "${attribute.translation().targetLanguage().name()}",
                "documentId": "${attribute.documentId()}",
                "selectedWordId": "${attribute.selectedWordId()}",
                "isPhrase": "${attribute.isPhrase()}",
                "_csrf": "${baseAttribute._csrf().getToken()}"
            }'
            hx-target="closest #translation-form-container-${attribute.selectedWordId()}"
            hx-swap="innerHTML">
                @if(attribute.translation().sourceWords().isEmpty())
                    <input type="hidden" name="sourceWords" value="">
                @endif
                @for(var word : attribute.translation().sourceWords())
                    <input type="hidden" name="sourceWords" value="${word}">
                @endfor
                <span>${level.getKey()}<span class="md:inline hidden">: </span></span>
                <input
                class="md:inline
                hidden cursor-pointer"
                value="${messages.get(TranslationLocalizationKey.valueOf(level.getValue().name().toUpperCase()))}"
                type="submit">
            </form>
        @endfor
    </div>
</div>
