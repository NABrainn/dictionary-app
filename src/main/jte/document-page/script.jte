@param int selectedWordId
@raw
<script class="
top-8 top-6 top-10 right-4 bottom-12 bottom-10 
border-b-secondary border-l-secondary border-t-secondary border-r-secondary border-l-transparent border-r-transparent border
border-l-2 border-r-2 border-t-2 border-b-2
px-0.5 pl-0.5 pr-0.5 w-[2px] min-w-[2px]">
    //ABOVE styles used here solely for tailwind live reload server to pick up

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
                        const familiarity = this.#getFamiliarity(lastTargetSelectable)
                        const spanElement = lastTargetSelectable?.children[1]
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
                        const spanElements = [...lastTargetSelectable?.children]
                                    .filter(word => word instanceof HTMLDivElement)
                                    .map(word => [...word.children])
                                    .filter(wordChildren => wordChildren.length)
                                    .map(wordChildren => wordChildren.at(2))
                                    .filter(spanElement => spanElement !== undefined)
                        if(spanElements) {
                            spanElements.forEach(span => {
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
                    const spanElements = [...selectable?.children]
                                .filter(word => word instanceof HTMLDivElement)
                                .map(word => [...word.children])
                                .filter(wordChildren => wordChildren.length)
                                .map(wordChildren => wordChildren.at(2))
                                .filter(spanElement => spanElement !== undefined)
                    if(spanElements) {
                        spanElements.forEach(span => {
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
                this.#unwrapLastSelection()
                this.#wrapCurrentSelection(phraseNodes)
                this.#destroySelector()
            }

            set data(data) {
                this.#data = data;
            }

            get data() {
                return this.#data ?? new Error('Data not initialized');
            }

            #wrapCurrentSelection(phraseNodes) {                
                const outerNodes = phraseNodes.filter((_, index) => index === 0 || index === phraseNodes.length - 1)
                const innerNodes = phraseNodes.slice(1, -1)
                this.#selectOuterNodes(outerNodes)
                this.#selectInnerNodes(innerNodes)
                this.#markOuterNodes(outerNodes)
                this.#markInnerNodes(innerNodes)
                const spans = this.#getSpans(phraseNodes)
                this.#highlightSpans(spans)
            }
            #selectOuterNodes(outerNodes) {
                outerNodes?.forEach((node, index) => {
                    node.classList.remove(...['border', 'border-2', 'border-transparent'])
                    if(index === 0) {
                        node.classList.add(...[
                            'border-b-secondary', 'border-l-secondary', 'border-t-secondary',
                            'border-b-2', 'border-l-2', 'border-t-2', 'bg-tertiary', 'pr-0.5'
                        ])
                    }
                    else {
                        node.classList.add(...[
                            'border-b-secondary', 'border-r-secondary', 'border-t-secondary',
                            'border-b-2', 'border-r-2', 'border-t-2', 'bg-tertiary', 'pl-0.5'
                        ])
                    }
                }) 
            }
            #selectInnerNodes(innerNodes) {
                innerNodes?.forEach(node => {
                    node.classList.remove(...['border', 'border-2', 'border-transparent'])
                    node.classList.add(...[
                        'border-b-secondary', 'border-t-secondary',
                        'border-b-2', 'border-t-2', 'bg-tertiary', 'px-0.5'
                    ]) 
                })
            }
            #markOuterNodes(outerNodes) {
                outerNodes?.forEach(node => {
                    if (node && node.classList) {
                        node.classList.add('new-phrase-part');
                    }
                });
            }
            #markInnerNodes(innerNodes) {
                innerNodes?.forEach(node => {
                    if (node && node.classList) {
                        node.classList.add('new-phrase-part');
                    }
                });
            }
            #getSpans(phraseNodes) {
                return phraseNodes
                    ?.flatMap(node => [...node.children])
                    .filter(node => node instanceof HTMLSpanElement)
            }
            #highlightSpans(spans) {
                spans?.forEach(node => {
                    node.classList.remove(...['bg-accent', 'bg-accent/80', 'bg-accent/60', 'bg-accent/40', 'bg-primary', 'text-primary'])
                    node.classList.add(...['text-accent'])
                })
            }

            #unwrapLastSelection() {
                const selectedNodes = this.#getSelectedNodes()
                this.#removeSelection(selectedNodes)
                this.#restoreHighlights(selectedNodes)
                this.#unmarkSelection(selectedNodes)
            }

            #removeSelection(selectedNodes) {                
                selectedNodes?.forEach((node, index) => {
                    node.classList.remove(...[
                        'border-b-secondary', 'border-l-secondary', 'border-t-secondary',
                        'border-b-2', 'border-l-2', 'border-t-2', 'bg-tertiary', 'pr-0.5',
                        'border-b-secondary', 'border-r-secondary', 'border-t-secondary',
                        'border-b-2', 'border-r-2', 'border-t-2', 'bg-tertiary', 'pl-0.5',
                        'border-b-secondary', 'border-t-secondary',
                        'border-b-2', 'border-t-2', 'bg-tertiary', 'px-0.5',
                    ])
                    node.classList.add(...['border', 'border-2', 'border-transparent'])
                })         
            }

            #restoreHighlights(selectedNodes) {
                const spanFamiliarities = selectedNodes
                    ?.flatMap(node => [...node.children])
                    .filter(node => node instanceof HTMLSpanElement)
                spanFamiliarities.forEach(node => {
                    const colors = this.#getColorsFromFamiliarity(node.dataset.familiarity)
                    node.classList.add(...colors)
                })
            }

            #getSelectedNodes() {
                return [...htmx.findAll('.new-phrase-part')]
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

            #unmarkSelection(selectedNodes) {
                selectedNodes?.forEach(node => node.classList.remove('new-phrase-part'))
            }

            #emitEvent() {
                const event = new Event('selectphrase')
                document.dispatchEvent(event)
            }

            #destroySelector(message) {
                if(message) {
                   console.error('destroying selector: ' + message)
                }
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
                return parseInt(window.getSelection()?.anchorNode?.parentElement?.parentElement?.id.substring(11))
            }

            #getFocusId() {
                return parseInt(window.getSelection()?.focusNode?.parentElement?.parentElement?.id.substring(11))
            }

        }
        customElements.define('phrase-selector', PhraseSelector);
    }
@endraw
</script>
<import-page-utils></import-page-utils>
<import-page selected-word-id="${selectedWordId}"></import-page>
<selectable-highlighter target-selectable-id="${selectedWordId}"></selectable-highlighter>
<phrase-selector></phrase-selector>



