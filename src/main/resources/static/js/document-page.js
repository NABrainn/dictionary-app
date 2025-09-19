window.documentPage = window.documentPage || {
    debug: false,
    closeAllTranslationForms() {
        if(this.debug) {
            console.log('cleaning up translation forms')
        }
        util.findAllByData({ key: 'is-translation-form-container', value: 'true' }).forEach(formContainer => util.removeInnerHTML(formContainer))
    },
    cleanupSelectedWord() {
        if(this.debug) {
            console.log('cleaning up selected word')
        }
        const familiarityColors = new Map([
            ['unknown', ['bg-accent', 'text-primary']],
            ['recognized', ['bg-accent/80', 'text-primary']],
            ['familiar', ['bg-accent/60', 'text-primary']],
            ['known', ['bg-primary', 'text-neutral']],
            ['ignored', ['bg-primary', 'text-neutral']]
        ])
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
    },
    cleanupSelectedPhrase() {
        if(this.debug) {
            console.log('cleaning up selected phrase')
        }
        const familiarityColors = new Map([
            ['unknown', ['bg-accent', 'text-primary']],
            ['recognized', ['bg-accent/80', 'text-primary']],
            ['familiar', ['bg-accent/60', 'text-primary']],
            ['known', ['bg-primary', 'text-neutral']],
            ['ignored', ['bg-primary', 'text-neutral']]
        ])
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
    },
    handleWordClick: (wordId) => {
        util.findAllByData({ key: 'is-word', value: 'true' })
        .filter(word => data.get(word, 'is-selected') === 'false')
        .filter(word => data.get(word, 'is-selectable') === 'true')
        .filter(word => data.get(word, 'id') === `${wordId}`)
        .flatMap(word => word.firstElementChild.nextElementSibling)
        .forEach(span => {
            data.set(span.parentElement, { key: 'is-selected', value: 'true' })
            util.replaceClasses(span, {
                toRemove: ['bg-accent', 'bg-accent/80', 'bg-accent/60', 'bg-accent/40', 'bg-primary', 'text-primary', 'text-accent', 'bg-tertiary'],
                toAdd: ['bg-tertiary', 'text-accent']
            })
        })
    },
    triggerPhraseSelection: (emitter) => {
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
        emitter(phraseNodes)
    },
    handlePhraseSelection: (phraseNodes) => {
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

        wrappedPhrase.content.forEach(node => node.classList.remove('border', 'border-2', 'border-transparent'))
        document.dispatchEvent(new Event('swapphrase'))
    },
    handlePhraseClick: (selectableId) => {
        util.findAllByData({ key: 'is-phrase', value: 'true' })
        .filter(node => data.get(node, 'is-selected') === 'false')
        .filter(node => data.get(node, 'id') === `${selectableId}`)
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
    },
    handlePersistWord: (word, familiarity) => {
        const familiarityColors = new Map([
            ['unknown', ['bg-accent', 'text-primary']],
            ['recognized', ['bg-accent/80', 'text-primary']],
            ['familiar', ['bg-accent/60', 'text-primary']],
            ['known', ['bg-primary', 'text-neutral']],
            ['ignored', ['bg-primary', 'text-neutral']]
        ])
        if(word.split(' ').length > 1) {
            util.findAllByData({ key: 'is-phrase', value: 'true' })
            .filter(phrase => data.get(phrase, 'is-saved') === 'false')
            .filter(phrase => data.get(phrase, 'is-selected') === 'true')
            .forEach(selectedPhrase => {
                data.set(selectedPhrase, { key: 'familiarity', value: familiarity })
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
            const matchedPhraseNodes = util.findAllByText({ text: word, nodes: util.findAllByData({ key: 'is-word-span', value: 'true' }) })
            .map(spanNodeList => spanNodeList.map(spanNode => spanNode.parentElement))
            .filter(wordList => wordList.every(word => data.get(word, 'is-wrapped') === 'false'))
            matchedPhraseNodes
            .forEach(phraseNodeList => {
                const wrapped = util.wrap({
                    wrapper: util.define('div', {
                        classList: ['inline-flex', 'relative', 'rounded', 'text-lg', 'gap-1', 'cursor-pointer', 'border', 'border-2', 'border-secondary'],
                        data: [
                            { key: 'id', value: data.get(phraseNodeList.at(0), 'id') },
                            { key: 'value', value: word },
                            { key: 'is-phrase', value: 'true' },
                            { key: 'is-saved', value: 'true' },
                            { key: 'is-selected', value: 'false' },
                            { key: 'length', value: word.split(' ').length }
                        ],
                    }),
                    content: phraseNodeList
                })

                util.insertBefore({
                    insert: util.define('div', {
                        classList: ['cursor-default', 'z-2', 'absolute', 'inline'],
                        id: phraseNodeList.at(0).firstElementChild.id,
                        data: [
                            { key: 'is-translation-form-container', value: 'true' }
                        ]
                    }),
                    before: wrapped.content.at(0)
                })
                util.replaceClasses(wrapped.wrapper, {
                    toRemove: ['bg-accent', 'bg-accent/80', 'bg-accent/60', 'bg-accent/40', 'bg-primary', 'text-primary', 'text-accent'],
                    toAdd: familiarityColors.get(familiarity)
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
                        toAdd: familiarityColors.get(familiarity).at(1)
                    }))
                })
                util.setHx({
                    element: wrapped.wrapper,
                    method: { key: 'hx-get', value: '/translations/find' },
                    target: 'closest #translation-form-container-' + data.get(phraseNodeList.at(0), 'id'),
                    swap: 'innerHTML',
                    trigger: 'click',
                    params: 'documentId,targetWord,id,isPhrase,isPersisted',
                    vals: {
                        documentId: data.get(phraseNodeList.at(0), 'document-id'),
                        targetWord: word,
                        id: data.get(phraseNodeList.at(0), 'id'),
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
            .filter(node =>
                data.get(node, 'value') === word
            )
            .forEach(word => {
                data.set(word, { key: 'familiarity', value: familiarity })
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
                        toAdd: familiarityColors.get(familiarity)
                    })
                }
            })
        }
    },
    handleUpdateFamiliarity(inputWord, familiarity) {
        const familiarityColors = new Map([
            ['unknown', ['bg-accent', 'text-primary']],
            ['recognized', ['bg-accent/80', 'text-primary']],
            ['familiar', ['bg-accent/60', 'text-primary']],
            ['known', ['bg-primary', 'text-neutral']],
            ['ignored', ['bg-primary', 'text-neutral']]
        ])

        if(inputWord.split(' ').length > 1) {
            util.findAllByData({ key: 'is-phrase', value: 'true' })
            .filter(phrase => data.get(phrase, 'value') === inputWord)
            .filter(phrase => data.get(phrase, 'is-saved') === 'true')
            .forEach(savedPhrase => {
                data.set(savedPhrase, { key: 'familiarity', value: familiarity })

                if(data.get(savedPhrase, 'is-selected') === 'true') {
                    util.replaceClasses(savedPhrase, {
                        toRemove: ['bg-accent', 'bg-accent/80', 'bg-accent/60', 'bg-accent/40', 'bg-primary', 'text-primary'],
                        toAdd: ['bg-tertiary', 'text-accent']
                    })
                    Array.from(savedPhrase.children)
                    .filter(child => data.get(child, 'is-word') === 'true')
                    .flatMap(word => [...word.children])
                    .filter(child => child instanceof HTMLSpanElement)
                    .forEach(span => {
                        util.replaceClasses(span, {
                            toRemove: ['text-accent', 'text-primary'],
                            toAdd: ['text-accent']
                        })
                    })
                }
                else {
                    util.replaceClasses(savedPhrase, {
                        toRemove: ['bg-accent', 'bg-accent/80', 'bg-accent/60', 'bg-accent/40', 'bg-primary', 'text-primary', 'text-accent'],
                        toAdd: familiarityColors.get(familiarity)
                    })
                    Array.from(savedPhrase.children)
                    .filter(child => data.get(child, 'is-word') === 'true')
                    .flatMap(word => [...word.children])
                    .filter(child => child instanceof HTMLSpanElement)
                    .forEach(span => {
                        util.replaceClasses(span, {
                            toRemove: ['text-accent', 'text-primary'],
                            toAdd: familiarityColors.get(familiarity).at(1)
                        })
                    })
                }
            })
        }
        else {
            util.findAllByData({ key: 'is-word', value: 'true' })
            .filter(word => data.get(word, 'value') === inputWord)
            .forEach(word => {
                data.set(word, { key: 'familiarity', value: familiarity })
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
                        toAdd: familiarityColors.get(familiarity)
                    })
                }
            })
        }
    }
}