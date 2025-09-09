if (!window.FormAdjuster) {
    class FormAdjuster {

        debug = false

        updatePositions(id) {
            const form = this.#getTranslationForm(id);
            this.#handleOverflow(form);
        }
        updateFormPosition(form) {
            this.#handleOverflow(form);
        }
        #getTranslationForm(id) {
            return htmx.find('#translation-form-container-' +  id);
        }
        #handleOverflow(form) {
            this.#updateCords(form, {
                top: 32,
                // bottom: 0,
                left: 8,
            })
            let eOverflow, neOverflow, nwOverflow, seOverflow, swOverflow, sOverflow, wOverflow;
            do {
                neOverflow = this.#handleNorthEastOverflow(form);
                nwOverflow = this.#handleNorthWestOverflow(form);
                seOverflow = this.#handleSouthEastOverflow(form);
                swOverflow = this.#handleSouthWestOverflow(form);
                sOverflow = this.#handleSouthOverflow(form);
            } while ([neOverflow, nwOverflow, seOverflow, swOverflow, sOverflow, eOverflow].some(result => result === true));

            // for(let i = 0; i < 30; i++) {
            //     neOverflow = this.#handleNorthEastOverflow(form);
            //     nwOverflow = this.#handleNorthWestOverflow(form);
            //     seOverflow = this.#handleSouthEastOverflow(form);
            //     swOverflow = this.#handleSouthWestOverflow(form);
            //     sOverflow = this.#handleSouthOverflow(form);
            // }
        }
        #handleNorthEastOverflow(form) {
            if(!form) {
                return
            }
            if (this.#isOverflowRight(form) && !this.#isOverflowBottom(form)) {
                this.#updateCords(form, {
                    top: 32,
                    // bottom: 0,
                    // left: 0,
                    right: 8
                })
                if(this.debug) {
                    console.log('overflow NE')
                }
                return true
            }
            return false

        }
        #handleNorthWestOverflow(form) {
            if(!form) {
                return
            }
            if (this.#isOverflowLeft(form) && !this.#isOverflowBottom(form)) {
                this.#updateCords(form, {
                    top: 32,
                    // bottom: 0,
                    // left: 0,
                    right: 8
                })
                if(this.debug) {
                    console.log('overflow NW')
                }
                return true
            }
            return false
        }
        #handleSouthEastOverflow(form) {
            if(!form) {
                return
            }
            if (this.#isOverflowRight(form) && this.#isOverflowBottom(form)) {
                this.#updateCords(form, {
                    // top: 0,
                    bottom: 32,
                    // left: 20,
                    right: 10
                })
                if(this.debug) {
                    console.log('overflow SE')
                }
                return true
            }
            return false
        }
        #handleSouthWestOverflow(form) {
            if(!form) {
                return
            }
            if (this.#isOverflowLeft(form) && this.#isOverflowBottom(form)) {
                this.#updateCords(form, {
                    // top: 30,
                    bottom: 32,
                    // left: 0,
                    right: -44
                })
                if(this.debug) {
                    console.log('overflow SW')
                }
                return true
            }
            return false
        }
        #handleSouthOverflow(form) {
            if(!form) {
                return
            }
            if (this.#isOverflowBottom(form)) {
                this.#updateCords(form, {
                    // top: 0,
                    bottom: 32,
                    left: 8,
                    // right: -120
                })
                if(this.debug) {
                    console.log('overflow S')
                }
                return true
            }
            return false
        }
        #handleWestOverflow(form) {
            if(!form) {
                return
            }
            if(this.#isOverflowLeft(form)) {
                this.#updateCords(form, {
                    top: 30,
                    // bottom: 0,
                    left: -50,
                    // right: 0
                })
                if(this.debug) {
                    console.log('overflow W')
                }
                return true
            }
            return false
        }
        #handleEastOverflow(form) {
            if(!form) {
                return
            }
            if(this.#isOverflowRight(form)) {
                this.#updateCords(form, {
                    // top: 30,
                    bottom: 40,
                    // left: 30,
                    right: -30
                })
                if(this.debug) {
                    console.log('overflow E')
                }
                return true
            }
            return false
        }
        #isOverflowLeft(form) {
            if(!form) {
                return
            }
            return form.getBoundingClientRect().left < 0;
        }
        #isOverflowRight(form) {
            if(!form) {
                return
            }
            return form.getBoundingClientRect().right > window.innerWidth;
        }
        #isOverflowBottom(form) {
            if(!form) {
                return
            }
            if(this.debug) {
                console.log('BoundingClientRect: ', form.getBoundingClientRect().bottom, 'window innerHeight: ', window.innerHeight)
            }
            return form.getBoundingClientRect().bottom > window.innerHeight;
        }
        #updateCords(form, cords) {
            form.style.removeProperty('left')
            form.style.removeProperty('right')
            form.style.removeProperty('top')
            form.style.removeProperty('bottom')

            if(this.debug) {
                console.log('properties removed: ', form.style)
            }
            for(const position in cords) {
                form.style[position] = cords[position]
            }
            if(this.debug) {
                console.log('coords updated: ', form.style)
            }
        }
    }
    window.formAdjuster = window.formAdjuster || new FormAdjuster()
}

window.documentPage = window.documentPage || {
    closeAllTranslationForms: () => util.findAllByData({ key: 'is-translation-form-container', value: 'true' }).forEach(formContainer => util.removeInnerHTML(formContainer)),
    cleanupSelectedWord: () => {
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
    cleanupSelectedPhrase: () => {
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
    handlePhraseSelection: () => {
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
        htmx.on('htmx:afterSwap', (e) => {
            formAdjuster.updateFormPosition(e.target.firstElementChild)
        })
        htmx.on('htmx:load', (e) => {
            formAdjuster.updateFormPosition(e.target.firstElementChild)
        })
        htmx.on('htmx:afterSettle', (e) => {
            formAdjuster.updateFormPosition(e.target.firstElementChild)
        })

        wrappedPhrase.content.forEach(node => node.classList.remove('border', 'border-2', 'border-transparent'))
        util.findAllByData({ key: 'is-translation-form-container', value: 'true' })
        .forEach(formContainer => util.removeInnerHTML(formContainer))

        document.dispatchEvent(new Event('createphrase'))
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
    }
}