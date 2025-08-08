@param int selectedWordId
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
                    this.#closeAllTranslationForms();
                    const targetSelectable = this.highlightTargetSelectable(parseInt(newValue));
                    const familiarity = this.#getFamiliarity(targetSelectable.element);
                    if(familiarity) {
                        this.highlightSelectables(targetSelectable.id, familiarity)
                    }
                }
            }

            #getFamiliarity(element) {
                    if(element) 
                        return this.familiarities.find(familiarity => element.classList.contains(familiarity))
            }

            updateSelectableId(id) {
                this.#updateId(id)
            }

            highlightSelectables(id, familiarity) {
                this.#markSelectableFamiliarities(id, familiarity)
                this.#highlightMarkedSelectables()
            }

            #markSelectableFamiliarities(id, familiarity) {
                const targetSelectableValue = htmx.find('#selectable-' + id).dataset.selectableValue
                console.log(targetSelectableValue)
                if(targetSelectableValue)
                    htmx.findAll('.selectable-' + targetSelectableValue).forEach(selectable => {
                        this.#updateFamiliarity(selectable, familiarity);
                    })
            }

            #highlightMarkedSelectables() {
                htmx.findAll('.selectable').forEach(selectable => {
                    const familiarity = this.#getFamiliarity(selectable);
                    this.#updateBgColor(selectable, familiarity)
                })
            }

            highlightTargetSelectable(id) {
                this.#resetNonTargetSelectables({
                    bgColorToRemove: 'bg-tertiary',
                    textColorToRemove: 'text-neutral',
                    textColorToAdd: 'text-primary',
                    bgColors: ['bg-accent', 'bg-accent/80', 'bg-accent/60', 'bg-accent/40']
                })
                const targetSelectable = htmx.find('#selectable-' + id);
                if (targetSelectable) {
                    this.#adjustTargetSelectableColors({
                        element: targetSelectable,
                        textColorToRemove: 'text-primary',
                        bgColorToAdd: 'bg-tertiary',
                        textColorToAdd: 'text-neutral'
                    });
                }
                return {id, element: targetSelectable}
            }

            #resetNonTargetSelectables(config) {
                document.querySelectorAll('[id^="selectable-"]').forEach(selectable => {
                    this.#removeHighlight(selectable, config.bgColorToRemove);
                    this.#restoreTextColor(selectable, config.bgColors, config.textColorToRemove, config.textColorToAdd)
                });
            }

            #removeHighlight(selectable, bgColorToRemove) {
                if (selectable.classList.contains(bgColorToRemove)) {
                    selectable.classList.remove(bgColorToRemove);
                }
            }

            #restoreTextColor(selectable, bgColors, textColorToRemove, textColorToAdd) {
                if (bgColors.some(color => selectable.classList.contains(color))) {
                    selectable.classList.remove(textColorToRemove);
                    selectable.classList.add(textColorToAdd);
                }
            }

            #updateFamiliarity(selectable, familiarity) {
                switch(familiarity) {
                    case 'unknown':
                        this.#updateClasses({
                            element: selectable,
                            classToAdd: 'unknown',
                            classesToRemove: this.familiarities
                        });
                        break;

                    case 'recognized':
                        this.#updateClasses({
                            element: selectable,
                            classToAdd: 'recognized',
                            classesToRemove: this.familiarities
                        });
                        break;

                    case 'familiar':
                        this.#updateClasses({
                            element: selectable,
                            classToAdd: 'familiar',
                            classesToRemove: this.familiarities
                        });
                        break;

                    case 'known':
                        this.#updateClasses({
                            element: selectable,
                            classToAdd: 'known',
                            classesToRemove: this.familiarities
                        });
                        break;

                    case 'ignored':
                        this.#updateClasses({
                            element: selectable,
                            classToAdd: 'ignored',
                            classesToRemove: this.familiarities
                        });
                        break;

                    default:
                        this.#updateClasses({
                            element: selectable,
                            classToAdd: 'ignored',
                            classesToRemove: classesToRemove
                        });
                        break;
                }
            }

            #updateClasses(config) {
                config.element.classList.remove(...config.classesToRemove)
                config.element.classList.add(config.classToAdd)
            }

            #updateBgColor(word, familiarity) {
                const colorsToRemove = ['bg-accent', 'bg-accent/80', 'bg-accent/60', 'bg-accent/40', 'bg-primary',  'text-neutral', 'text-primary'];
                const selectedWordBgColor = 'bg-tertiary'
                switch(familiarity) {
                    case 'unknown':
                        if(!word.classList.contains(selectedWordBgColor)) {
                            this.#updateColors({
                                element: word,
                                colorsToRemove: colorsToRemove,
                                bgColorToAdd: 'bg-accent',
                                textColorToAdd: 'text-primary'
                            });
                        }
                        break;

                    case 'recognized':
                        if(!word.classList.contains(selectedWordBgColor)) {
                            this.#updateColors({
                                element: word,
                                colorsToRemove: colorsToRemove,
                                bgColorToAdd: 'bg-accent/80',
                                textColorToAdd: 'text-primary'
                            });
                        }
                        break;

                    case 'familiar':
                        if(!word.classList.contains(selectedWordBgColor)) {
                            this.#updateColors({
                                element: word,
                                colorsToRemove: colorsToRemove,
                                bgColorToAdd: 'bg-accent/60',
                                textColorToAdd: 'text-primary'
                            });
                        }   
                        break;

                    case 'known':
                        if(!word.classList.contains(selectedWordBgColor)) {
                            this.#updateColors({
                                element: word,
                                colorsToRemove: colorsToRemove,
                                bgColorToAdd: 'bg-primary',
                                textColorToAdd: 'text-neutral'
                            });
                        }
                        break;

                    case 'ignored':
                        if(!word.classList.contains(selectedWordBgColor)) {
                            this.#updateColors({
                                element: word,
                                colorsToRemove: colorsToRemove,
                                bgColorToAdd: 'bg-primary',
                                textColorToAdd: 'text-neutral'
                            });
                        }
                        break;

                    default:
                        if(!word.classList.contains(selectedWordBgColor)) {
                            this.#updateColors({
                                element: word,
                                colorsToRemove: colorsToRemove,
                                bgColorToAdd: 'bg-primary',
                                textColorToAdd: 'text-neutral'
                            });
                        }
                        break;
                }
            }

            #updateColors(config) {
                config.element.classList.remove(...config.colorsToRemove)
                config.element.classList.add(config.bgColorToAdd)
                config.element.classList.add(config.textColorToAdd) 
            }

            #closeAllTranslationForms() {
                document.querySelectorAll('[id^="translation-form-"]').forEach(form => {
                    form.innerHTML = '';
                });
            }

            #adjustTargetSelectableColors(config) {
                if(config.element.classList.contains(config.textColorToRemove)) {
                    config.element.classList.remove(config.textColorToRemove);
                }
                config.element.classList.add(config.bgColorToAdd, config.textColorToAdd);
            }

            #updateId(id) {
                this.setAttribute('target-selectable-id', id);
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
                const phraseChildNodes = this.#getPhraseChildNodes();

                if(!phraseChildNodes) 
                    return this.#destroySelector()

                if(phraseChildNodes.length > 5) 
                    return this.#destroySelector()

                const phraseText = this.#getPhraseText(phraseChildNodes)
                if(!phraseText) 
                    return this.#destroySelector()

                const phraseParentNodes = this.#getPhraseParentNodes(phraseChildNodes)
                if(!phraseParentNodes) 
                    return this.#destroySelector()

                if(!phraseParentNodes.every(node => node.classList.contains('selectable'))) 
                    return this.#destroySelector()

                const paragraph = this.#getParagraph(phraseParentNodes)
                if(!paragraph) 
                    return this.#destroySelector()

                @raw
                //hacky
                @endraw

                if(htmx.find('.selected-phrase')) {
                    this.#removeUnusedSelections().then(() => {
                        this.#wrapWords(paragraph, phraseParentNodes)                
                        this.#emitEvent()
                        this.#destroySelector()
                    })
                    this.#data = {
                    targetWords: phraseText,
                    selectableId: phraseParentNodes.at(0).dataset.id,
                    familiarities: [...phraseChildNodes]
                                    .map(node => [...node.classList]
                                        .filter(cls => this.#familiarities.includes(cls)))
                                    .map(list => !list.length ? ['unknown'] : list)
                    }
                }
                else {
                    this.#data = {
                        targetWords: phraseText,
                        selectableId: phraseParentNodes.at(0).dataset.id,
                        familiarities: [...phraseChildNodes]
                                        .map(node => [...node.classList]
                                            .filter(cls => this.#familiarities.includes(cls)))
                                        .map(list => !list.length ? ['unknown'] : list)
                    }
                    this.#wrapWords(paragraph, phraseParentNodes)                
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

            #destroySelector() {
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

            #getParagraph(phraseParentNodes) {
                return phraseParentNodes.at(0).parentElement;
            }

            #getPhraseChildNodes() {
                if(this.#getAnchorId() === this.#getFocusId()) return
                const phraseNodes = []
                for(let i = this.#getAnchorId(); i <= this.#getFocusId(); i++) {
                    phraseNodes.push(htmx.find('#selectable-' + i))
                }
                return phraseNodes;
            }
            
            #getPhraseParentNodes(phraseChildNodes) {
                return phraseChildNodes
                            .filter(node => node !== null)
                            .map(node => node.parentElement)
            }

            #getPhraseText(phraseNodes) {
                return phraseNodes
                            .filter(node => node !== null)
                            .map(node => node.innerText).join(" ")
            }

            #getAnchorId() {
                return parseInt(window.getSelection()?.anchorNode?.parentElement.id.substring(11))
            }

            #getFocusId() {
                return parseInt(window.getSelection()?.focusNode?.parentElement.id.substring(11))
            }

        }
        customElements.define('phrase-selector', PhraseSelector);
    }
</script>
<import-page selected-word-id="${selectedWordId}"></import-page>
<selectable-highlighter target-selectable-id="${selectedWordId}"></selectable-highlighter>
<phrase-selector></phrase-selector>



