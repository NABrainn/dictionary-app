@param int selectedWordId
@raw
<script class="top-8 top-6 top-10 right-4 bottom-12 bottom-10">
    if (!customElements.get('import-page')) {
        class ImportPage extends HTMLElement {

            static observedAttributes = ['selected-word-id'];

            positions = {
                left: 'left-2',
                right: '',
                top: 'top-10',
                bottom: ''
            }

            constructor() {
                super();
            }

            connectedCallback() {
                this.setAttribute('selected-word-id', 0);
            }
            updateWordId(id) {
                this.#updateId(id)
            }

            updatePositions(id) {
                this.#updateId(id)
                const form = this.#getTranslationForm();
                this.#handleOverflow(form);
                this.#updateFormData();
            }

            #closeAllTranslationForms() {
                document.querySelectorAll('[id^="translation-form-"]').forEach(form => {
                    form.innerHTML = '';
                });
            }

            #updateFormData() {
                const wordId = this.getAttribute('selected-word-id')
                htmx.findAll('.left-' + wordId).forEach((input) => {
                    input.value=this.positions.left
                })
                htmx.findAll('.right-' + wordId).forEach((input) => {
                    input.value=this.positions.right
                })
                htmx.findAll('.top-' + wordId).forEach((input) => {
                    input.value=this.positions.top
                })
                htmx.findAll('.bottom-' + wordId).forEach((input) => {
                    input.value=this.positions.bottom
                })
            }

            #updateId(id) {
                this.setAttribute('selected-word-id', id);
            }

            #getTranslationForm() {
                return htmx.find('#translation-form-container-' + this.getAttribute('selected-word-id'));
            }

            #handleOverflow(form) {
                this.#setDefaultPositions({
                    form: form,
                    left: 2,
                    top: 10
                })
                this.#handleNorthEastOverflow(form)
                this.#handleNorthWestOverflow(form)
                this.#handleSouthEastOverflow(form)
                this.#handleSouthWestOverflow(form)
                this.#handleSouthOverflow(form)                
            }

            #handleNorthEastOverflow(form) {
                if (this.#isOverflowRight(form) && !this.#isOverflowBottom(form)) {
                    this.#adjustPosition({
                        form: form,
                        top: 8,
                        right: 4
                    });
                }
            }
            #handleNorthWestOverflow(form) {
                if (this.#isOverflowLeft(form) && !this.#isOverflowBottom(form)) {
                    this.#adjustPosition({
                        form: form,
                        top: 0,
                        bottom: 0,
                        left: 0,
                        right: 0
                    });
                }
            }
            #handleSouthEastOverflow(form) {
                if (this.#isOverflowRight(form) && this.#isOverflowBottom(form)) {
                    this.#adjustPosition({
                        form: form,
                        bottom: 10,
                        right: 4
                    });
                }
            }
            #handleSouthWestOverflow(form) {
                if (this.#isOverflowLeft(form) && this.#isOverflowBottom(form)) {
                    this.#adjustPosition({
                        form: form,
                        top: 0,
                        bottom: 0,
                        left: 0,
                        right: 0
                    });
                }
            }

            #handleSouthOverflow(form) {
                if (!this.#isOverflowLeft(form) && !this.#isOverflowRight(form) && this.#isOverflowBottom(form)) {
                    this.#adjustPosition({
                        form: form,
                        bottom: 10
                    });
                }
            }

            #isOverflowLeft(form) {
                return form.getBoundingClientRect().left > window.innerWidth;
            }

            #isOverflowRight(form) {
                return form.getBoundingClientRect().right > window.innerWidth;
            }

            #isOverflowBottom(form) {
                return form.getBoundingClientRect().bottom > window.innerHeight;
            }

            #adjustPosition(config) {
                this.#cleanupPositions(config)

                const rightPos = this.#assignRightPosition(config.right);
                const leftPos = this.#assignLeftPosition(config.left);
                const bottomPos = this.#assignBottomPosition(config.bottom);
                const topPos = this.#assignTopPosition(config.top);

                const positions = {
                    left: leftPos ?? '',
                    right: rightPos ?? '',
                    top: topPos ?? '',
                    bottom: bottomPos ?? ''
                }

                for(const [key, position] of Object.entries(positions)) {
                    if(position !== '') {
                        this.#updateClassList(config.form, position)
                    }
                }

                this.positions = positions
            }

            #updateClassList(form, value) {
                form.classList.add(value)
            }

            #assignRightPosition(value) {
                if(value !== undefined) {
                    if(value >= 0) {
                        return "right-" + value;
                    }destroySelector
                    return "-right-" + Math.abs(value)
                }
            }

            #assignLeftPosition(value) {
                if(value !== undefined) {
                    if(value >= 0) {
                        return "left-" + value;
                    }
                    return "-left-" + Math.abs(value)
                }
            }

            #assignBottomPosition(value) {
                if(value !== undefined) {
                    if(value >= 0) {
                        return "bottom-" + value;
                    }
                    return "-bottom-" + Math.abs(value)
                }
            }

            #assignTopPosition(value) {
                if(value !== undefined) {
                    if(value >= 0) {
                        return "top-" + value;
                    }
                    return "-top-" + Math.abs(value)
                }
            }

            #cleanupPositions(config) {
                const positionClasses = Array.from(config.form.classList).filter(cls =>
                    cls.match(/^(right-|left-|top-|bottom-|-right-|-left-|-top-|-bottom-)\d+$/)
                );
                positionClasses.forEach(cls => config.form.classList.remove(cls));
                
            }
            #setDefaultPositions(config) {
                this.#adjustPosition(config)
            }
        }

        customElements.define('import-page', ImportPage);
    }

    if (!customElements.get('selectable-highlighter')) {
        class SelectableHighlighter extends HTMLElement {

            static observedAttributes = ['target-selectable-id'];
            familiarities = ['unknown', 'recognized', 'familiar', 'known', 'ignored']

            constructor() {
                super();
            }

            connectedCallback() {
                this.setAttribute('target-selectable-id', 0);
            }

            attributeChangedCallback(name, oldValue, newValue) {
                if (name === 'target-selectable-id' && oldValue !== newValue) {
                    this.closeLastTranslationForm()
                    this.#fadeLastTargetSelectable()
                    this.#highlightTargetSelectable()
                }
            }

            updateSelectableId(id) {
                this.#updateId(id)
            }

            highlightSelectables(targetWord, familiarity) {
                const selectables = this.#updateFamiliarities(targetWord.replace(" ", "-"), familiarity)
                
                selectables.forEach(selectable => {
                    if(this.#isWord(selectable)) {
                        if(!this.#isSelected(selectable)) {
                            const spanElement = selectable?.children[1]
                            const colorsToAdd = this.#getColorsFromFamiliarity(familiarity)
                            this.#removeColors({
                                selectable: spanElement, 
                                colorsToRemove: ['bg-accent', 'bg-accent/80', 'bg-accent/60', 'bg-accent/40', 'bg-primary', 'text-primary']
                            })
                            this.#addColors({
                                selectable: spanElement,
                                colorsToAdd: colorsToAdd
                            })
                        }
                    }
                    else {
                        if(!this.#isSelected(selectable)) {
                            const colorsToAdd = this.#getColorsFromFamiliarity(familiarity)

                            this.#removeColors({
                                selectable: selectable, 
                                colorsToRemove: ['bg-tertiary', 'text-neutral']
                            })
                            this.#addColors({
                                selectable: selectable,
                                colorsToAdd: colorsToAdd
                            })
                            const spans = [...selectable?.children]
                                                    .map(child => child.children?.[1])
                                                    .filter(span => span !== undefined)
                            if(spans) {
                                spans.forEach(span => {
                                    this.#removeColors({
                                        selectable: span, 
                                        colorsToRemove: ['bg-tertiary', 'text-neutral']
                                    })
                            
                                })
                            }
                        }
                    }
                })
            }

            #updateFamiliarities(targetWord, familiarity) {
                const familiarities = ['unknown', 'recognized', 'familiar', 'known', 'ignored']
                const selectables = htmx.findAll(`.value-${targetWord}`)                
                selectables.forEach(selectable => {
                    selectable.classList.remove(...familiarities)
                    selectable.classList.add(familiarity)
                })
                return selectables
                
            }

            #highlightTargetSelectable() {
                const targetSelectable = this.#getSelectable();
                if(targetSelectable) {
                    this.#markTargetSelectable(targetSelectable)
                    this.#colorSelectable(targetSelectable)
                }
            }

            closeLastTranslationForm() {
                const form = htmx.find('#translation-form')
                if(form) {
                    form.outerHTML = ''
                }
            }

            #isSelected(selectable) {
                return selectable.classList.contains('target-selectable')
            }

            #fadeLastTargetSelectable() {
                const lastTargetSelectable = this.#getMarkedTargetSelectable();
                if(lastTargetSelectable) {
                    if(this.#isWord(lastTargetSelectable)) {
                        const spanElement = lastTargetSelectable?.children[1]
                        const familiarity = this.#getFamiliarity(spanElement)
                        const colorsToAdd = this.#getColorsFromFamiliarity(familiarity)
                        this.#removeColors({
                            selectable: spanElement, 
                            colorsToRemove: ['bg-tertiary', 'text-neutral']
                        })
                        this.#addColors({
                            selectable: spanElement,
                            colorsToAdd: colorsToAdd
                        })
                        this.#unmarkTargetSelectable(lastTargetSelectable);
                    }
                    else {
                        const familiarity = this.#getFamiliarity(lastTargetSelectable)
                        const colorsToAdd = this.#getColorsFromFamiliarity(familiarity)

                        this.#removeColors({
                            selectable: lastTargetSelectable, 
                            colorsToRemove: ['bg-tertiary', 'text-neutral']
                        })
                        this.#addColors({
                            selectable: lastTargetSelectable,
                            colorsToAdd: colorsToAdd
                        })
                        const spans = [...lastTargetSelectable?.children]
                                                .map(child => child.children?.[1])
                                                .filter(span => span !== undefined)
                        if(spans) {
                            spans.forEach(span => {
                                this.#removeColors({
                                    selectable: span, 
                                    colorsToRemove: ['bg-tertiary', 'text-neutral']
                                })
                         
                            })
                        }
                        this.#unmarkTargetSelectable(lastTargetSelectable);

                    }
                }
            }

            #getFamiliarity(element) {
                const familiarities = ['unknown', 'recognized', 'familiar', 'known', 'ignored']
                return familiarities.filter(fam => element.classList.contains(fam)).at(0)
            }

            #colorSelectable(selectable) {
                if(this.#isWord(selectable)) {
                    const spanElement = selectable?.children[1]
                    this.#removeColors({
                        selectable: spanElement, 
                        colorsToRemove: ['bg-accent', 'bg-accent/80', 'bg-accent/60', 'bg-accent/40', 'bg-primary', 'text-primary']
                    })
                    this.#addColors({
                        selectable: spanElement,
                        colorsToAdd: ['bg-tertiary', 'text-neutral']
                    })
                }
                else {
                    this.#removeColors({
                        selectable: selectable, 
                        colorsToRemove: ['bg-accent', 'bg-accent/80', 'bg-accent/60', 'bg-accent/40', 'bg-primary', 'text-primary']
                    })
                    this.#addColors({
                        selectable: selectable,
                        colorsToAdd: ['bg-tertiary', 'text-neutral']
                    })
                    const spans = [...selectable?.children].map(child => child.children?.[1]).filter(span => span !== undefined)
                    if(spans) {
                        spans.forEach(span => {
                            this.#removeColors({
                                selectable: span, 
                                colorsToRemove: ['bg-accent', 'bg-accent/80', 'bg-accent/60', 'bg-accent/40', 'bg-primary', 'text-primary']
                            })
                            this.#addColors({
                                selectable: span,
                                colorsToAdd: ['bg-tertiary', 'text-neutral']
                            })
                        })
                    }
                }
            }
            
            #getColorsFromFamiliarity(familiarity) {
                switch(familiarity) {
                    case 'unknown':
                        return ['bg-accent', 'text-primary']

                    case 'recognized':
                        return ['bg-accent/80', 'text-primary']

                    case 'familiar':
                        return ['bg-accent/60', 'text-primary']

                    case 'known':
                        return ['bg-primary', 'text-neutral']

                    default:
                        return ['bg-primary', 'text-neutral']
                }
            }

            #unmarkTargetSelectable(selectable) {
                selectable.classList.remove('target-selectable');
            }

            #getMarkedTargetSelectable() {
                return htmx.find('.target-selectable')
            }

            #markTargetSelectable(targetSelectable) {
                targetSelectable.classList.add('target-selectable');
            }

            #isWord(selectable) {
                return selectable.classList.contains('word');
            }

            #addColors(config) {
                config.selectable.classList.add(...config.colorsToAdd)
            }

            #removeColors(config) {
                config.selectable.classList.remove(...config.colorsToRemove)
            }

            #updateId(id) {
                this.setAttribute('target-selectable-id', id);
            }

            #getSelectable() {
                return htmx.find('#selectable-' + this.getAttribute('target-selectable-id'))
            }

            #getType(selectable) {
                return selectable.classList.contains('word') ? 'word' : 'phrase'
            }
        }
        customElements.define('selectable-highlighter', SelectableHighlighter);
    }
    if (!customElements.get('phrase-selector')) {
        class PhraseSelector extends HTMLElement {
            #data = {}
            #familiarities = ['unknown', 'recognized', 'familiar', 'known', 'ignored']
            constructor() {
                super()
                this.#initializeListener()
            }

            #selectPhrase() {
                const phraseNodes = this.#getPhraseNodes();
                if(!phraseNodes) 
                    return this.#destroySelector('phrase child nodes cant be undefined')

                if(phraseNodes.length > 5) 
                    return this.#destroySelector('selection too long')

                if(phraseNodes.some(node => node?.classList.contains('target-selectable')))
                    return this.#destroySelector('cannot create phrase from selected word')

                const phraseText = this.#getPhraseText(phraseNodes)
                if(!phraseText) 
                    return this.#destroySelector('selection is empty')

                
                if(!phraseNodes.every(node => node?.classList.contains('selectable')))
                    return this.#destroySelector('all phrase parent nodes must be selectable')

                const paragraph = this.#getParagraph(phraseNodes)
                if(!paragraph) 
                    return this.#destroySelector('paragraph cant be empty')

                //hacky

                if(htmx.find('.selected-phrase')) {
                    this.#removeUnusedSelections().then(() => {
                        this.#wrapWords(paragraph, phraseNodes)                
                        this.#emitEvent()
                        this.#destroySelector()
                    })
                    this.#data = {
                    targetWords: phraseText,
                    selectableId: phraseNodes.at(0).dataset.id,
                    familiarities: [...phraseNodes]
                                    .map(node => [...node.classList]
                                        .filter(cls => this.#familiarities.includes(cls)))
                                    .map(list => !list.length ? ['unknown'] : list)
                    }
                }
                else {
                    this.#data = {
                        targetWords: phraseText,
                        selectableId: phraseNodes.at(0).dataset.id,
                        familiarities: [...phraseNodes]
                                        .map(node => [...node.classList]
                                            .filter(cls => this.#familiarities.includes(cls)))
                                        .map(list => !list.length ? ['unknown'] : list)
                    }
                    this.#wrapWords(paragraph, phraseNodes)                
                    this.#emitEvent()
                    this.#destroySelector()
                }
            }

            set data(data) {
                this.#data = data;
            }

            get data() {
                return this.#data ?? new Error('Data not initialized');
            }

            #wrapWords(paragraph, phraseNodes) {
                const phraseContainer = this.#createPhraseContainer()
                paragraph.insertBefore(phraseContainer, phraseNodes.at(0))
                phraseNodes.forEach(node => node.remove())
                phraseNodes.forEach((node, id) => {
                    phraseContainer.appendChild(node)
                    if(id < phraseNodes.length - 1) {
                        phraseContainer.appendChild(document.createTextNode("\u00A0"))
                    }
                })
            }

            #emitEvent() {
                const event = new Event('selectphrase')
                document.dispatchEvent(event)
            }

            #destroySelector(message) {
                // if(message) {
                //    console.err or('destroying selector: ' + message)
                // }
                window.getSelection().removeAllRanges()
                return
            }

            #initializeListener() {
                const content = htmx.find('#page-content')
                content.addEventListener('mouseup', () => {
                    this.#selectPhrase()
                })
            }

            #removeUnusedSelections() {
                return htmx.ajax('GET', '/translations/unselect-phrase', {target: '.selected-phrase', swap: 'outerHTML', values: {
                    targetWords: this.data.targetWords,
                    selectableId: this.data.selectableId,
                    familiarities: this.data.familiarities,
                    documentId: htmx.find('#page-content').dataset.documentId
                }})
            }

            #createPhraseContainer() {
                const container = document.createElement('div')
                container.classList.add("inline-block", "relative", "rounded", "text-lg", "gap-1", "cursor-pointer", "border", "border-3", "border-secondary", "selected-phrase")
                return container
            }

            #getParagraph(phraseChildNodes) {
                return phraseChildNodes.at(0).parentElement;
            }

            #getPhraseNodes() {
                if(this.#getAnchorId() === this.#getFocusId()) return
                const phraseNodes = []
                for(let i = this.#getAnchorId(); i <= this.#getFocusId(); i++) {
                    phraseNodes.push(htmx.find('.word-' + i))
                }
                return phraseNodes.filter(node => node !== null);
            }

            #getPhraseText(phraseNodes) {
                return phraseNodes
                            .filter(node => node !== null)
                            .map(node => node.innerText).join(" ")
            }

            #getAnchorId() {
                return parseInt(window.getSelection()?.anchorNode?.parentElement?.parentElement.id.substring(11))
            }

            #getFocusId() {
                return parseInt(window.getSelection()?.focusNode?.parentElement?.parentElement.id.substring(11))
            }

        }
        customElements.define('phrase-selector', PhraseSelector);
    }
@endraw
</script>
<import-page selected-word-id="${selectedWordId}"></import-page>
<selectable-highlighter target-selectable-id="${selectedWordId}"></selectable-highlighter>
<phrase-selector></phrase-selector>



