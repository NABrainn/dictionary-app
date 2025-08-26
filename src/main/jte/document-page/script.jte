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
                [...htmx.findAll('div')]
                    .filter(element => element.id.includes('translation-form-container-'))
                    .forEach(form => form.innerHTML = '')
            }

            #updateFormData() {
                const wordId = this.getAttribute('selected-word-id')
                htmx.findAll('.left-' + wordId).forEach((input) => {
                    input.value = this.positions.left
                })
                htmx.findAll('.right-' + wordId).forEach((input) => {
                    input.value = this.positions.right
                })
                htmx.findAll('.top-' + wordId).forEach((input) => {
                    input.value = this.positions.top
                })
                htmx.findAll('.bottom-' + wordId).forEach((input) => {
                    input.value = this.positions.bottom
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
                    }
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
    //TODO make this a word highlighter, phrase highlighter will select and highlight phrases in isolation
    if (!customElements.get('selectable-highlighter')) {
        class SelectableHighlighter extends HTMLElement {

            static observedAttributes = ['selected-word-id'];
            familiarities = ['unknown', 'recognized', 'familiar', 'known', 'ignored']

            constructor() {
                super();
            }

            connectedCallback() {
                this.setAttribute('selected-word-id', 0);
            }

            attributeChangedCallback(name, oldValue, newValue) {
                if (name === 'selected-word-id' && oldValue !== newValue) {
                    const formContainers = util.findAllByData({
                        key: 'is-translation-form-container',
                        value: 'true'
                    })
                    this.#closeAllTranslationForms(formContainers)                    
                    this.#fadeLastSelectedWord()
                    this.#highlightSelectedWord()
                }
            }
            #closeAllTranslationForms(forms) {
                forms.forEach(form => util.removeInnerHTML(form))
            }
            #fadeLastSelectedWord() {
                const lastSelectedWord = this.#getSelectedWord();                                                                                    
                if(lastSelectedWord) {
                    if(this.#isWord(lastSelectedWord)) {
                        const familiarity = this.#getFamiliarity(lastSelectedWord)
                        const colorsToRemove = ['bg-tertiary', 'text-neutral']
                        
                        const colorsToAdd = this.#getColorsFromFamiliarity(familiarity)
                        this.#updateColors({
                            node: lastSelectedWord,
                            toRemove: colorsToRemove,
                            toAdd: colorsToAdd
                        })                        
                        this.#removeWordSelection(lastSelectedWord);
                    }
                }
            }
            #highlightSelectedWord() {
                const wordToSelect = this.#getWordToSelect();                                                
                if(wordToSelect) {
                    this.#setIsSelected(wordToSelect)                    
                    this.#colorSelectedWord(wordToSelect)
                }
            }
            updateSelectableId(id) {
                this.#updateId(id)
            }

            highlightSelectables(targetWord, familiarity) {
                const selectableWords = [
                    ...util.findAllByData({
                        key: 'type',
                        value: 'word-span'
                    })             
                ]                
                this.#updateFamiliarities(selectableWords, familiarity)    
                const nonSelectedWords = selectableWords
                    .filter(selectable => selectable.dataset.isWordSelected === 'false')
                    .filter(span => span.dataset.value === targetWord)                    
                nonSelectedWords.forEach(selectable => {                    
                    if(this.#isWord(selectable)) {
                        const colorsToRemove = ['bg-accent', 'bg-accent/80', 'bg-accent/60', 'bg-accent/40', 'bg-primary', 'text-primary']
                        const colorsToAdd = this.#getColorsFromFamiliarity(familiarity)             
                        this.#updateColors({
                            node: selectable,
                            toRemove: colorsToRemove,
                            toAdd: colorsToAdd
                        })
                    }
                })
            }
            #updateFamiliarities(selectableWords, familiarity) {                
                selectableWords.forEach(selectable => {
                    selectable.dataset.familiarity = familiarity
                })    
            }
            #getSelectedWord() {       
                return util.findByData({
                    key: 'is-word-selected',
                    value: 'true'
                })
            }
            #removeWordSelection(word) {
                word.dataset.isWordSelected = 'false'
            }
            #getWordToSelect() {                
                return htmx.find(`#word-span-${this.getAttribute('selected-word-id')}`)
            }

            #isSelected(selectable) {
                return selectable.dataset.isSelectedWord === 'true'
            }

            #getFamiliarity(element) {
                const familiarities = ['unknown', 'recognized', 'familiar', 'known', 'ignored']                                
                return familiarities.find(familiarity => familiarity === element.dataset.familiarity)
            }

            #colorSelectedWord(word) {
                if(this.#isWord(word)) {
                    this.#updateColors({
                        node: word,
                        toRemove: ['bg-accent', 'bg-accent/80', 'bg-accent/60', 'bg-accent/40', 'bg-primary', 'text-primary'],
                        toAdd: ['bg-tertiary', 'text-neutral']
                    })
                }
            }

            #updateColors(config) {
                util.replaceClasses(config)
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

            #setIsSelected(word) {
                word.dataset.isWordSelected = 'true';
            }

            #isWord(selectable) {
                return selectable.dataset.selectableType === 'word';
            }

            #updateId(id) {
                this.setAttribute('selected-word-id', id);
            }
        }
        customElements.define('selectable-highlighter', SelectableHighlighter);
    }
    if (!customElements.get('phrase-selector')) {
        class PhraseSelector extends HTMLElement {
            selectedPhraseData = {
                value: '',
                id: 0,
            }
            #familiarities = ['unknown', 'recognized', 'familiar', 'known', 'ignored']
            constructor() {
                super()
                this.#initializeListener()
            }
            updatePhrasesUI(phrase, familiarity) {
                const selectableSpanNodes = [...htmx.findAll('.selectable-text')]
                const phrases = util.findAllByInnerText({
                    innerText: phrase,
                    nodes: selectableSpanNodes
                })
                .map(arr => arr.map(span => span.parentElement))     
                //update non-selected phrases                   
                phrases
                    .filter(phrase => phrase?.parentElement?.classList.contains('paragraph'))
                    .filter(arr => arr.length)
                    .forEach(phrase => {
                        phrase.forEach(div => {
                            div.classList.remove('border', 'border-2')
                            const children = [...div.children]
                            children
                                .filter(child => child instanceof HTMLSpanElement)
                                .forEach(span => {
                                    const colorsToRemove = [
                                        'bg-accent', 'bg-accent/80', 'bg-accent/60', 'bg-accent/40', 'bg-primary', 'text-primary', 'pointer-events-none'
                                    ]
                                    const colorsToAdd = this.#getColorsFromFamiliarity(familiarity);
                                    span.classList.remove(...colorsToRemove)
                                    span.classList.add(...colorsToAdd)
                                })
                        })
                        util.wrap({
                            content: phrase,
                            wrapper: util.define({
                                type: 'div',
                                classList: [
                                    'inline-flex', 'relative', 'rounded', 'text-lg', 'gap-1', 'cursor-pointer', 'border', 'border-2', 'border-tertiary', 
                                    ...this.#getColorsFromFamiliarity(familiarity)
                                ]
                            })
                        })
                    })

            }
            #selectPhrase() {
                const phraseNodes = this.#getPhraseNodes();
                if(!phraseNodes) 
                    return this.#destroySelector('phrase child nodes cant be undefined')

                if(phraseNodes.length < 2) 
                    return this.#destroySelector('selection too short')

                if(phraseNodes.length > 5) 
                    return this.#destroySelector('selection too long')

                if(phraseNodes.some(node => node?.dataset.isWordSelected === 'true'))
                    return this.#destroySelector('cannot create phrase from selected word')

                const phraseText = this.#getPhraseText(phraseNodes)
                if(!phraseText) 
                    return this.#destroySelector('selection is empty')

                if(!phraseNodes.every(node => node?.classList.contains('selectable')))
                    return this.#destroySelector('all phrase parent nodes must be selectable')                              
                
                this.#unwrapLastSelection()
                this.#closeAllTranslationForms()
                this.#wrapCurrentSelection(phraseNodes)
                this.#emitEvent()
                this.#destroySelector()
            }
            #unwrapLastSelection() {
                const container = this.#getSelectedPhrase()
                
                if(container) {
                    const familiarities = ['unknown', 'recognized', 'familiar', 'known', 'ignored'];
                    const matchedClass = familiarities.find(cls => container.classList.contains(cls));
                    if(!matchedClass) {
                        const phraseNodes = this.#unwrap(container)
                        this.#unstylePhraseNodes(phraseNodes)  
                    } 
                    else {
                        container.classList.remove('bg-tertiary', 'selected-phrase')
                        const children = [...container.children]

                        const selectedPhraseForm = children
                            .filter(child => child.id === 'selected-phrase-form')
                        selectedPhraseForm
                            .forEach(child => child.id = `translation-form-container-${this.selectedPhraseData.id}`) 

                        const selectedPhraseNodes = children
                            .filter(child => child.id !== 'selected-phrase-form')
                            .flatMap(child => [...child.children])
                            .filter(grandchild => grandchild instanceof HTMLSpanElement)
                        selectedPhraseNodes
                            .forEach(child => {
                                child.classList.remove('bg-accent')
                                child.classList.add(...this.#getColorsFromFamiliarity(child.dataset.familiarity))
                            })
                        

                    }
                    
                }
            }
            #unstylePhraseNodes(phraseNodes) {
                const stylesToRemove = ['border', 'border-2', 'border-transparent']
                phraseNodes.forEach(node => node.classList.add(...stylesToRemove))
                const spans = [...phraseNodes]
                    .flatMap(child => [...child.children])
                    .filter(grandchild => grandchild instanceof HTMLSpanElement)
                spans.forEach(span => {
                    const colorsFromFamiliarity = this.#getColorsFromFamiliarity(span.dataset.familiarity)
                    const spanStylesToAdd = [...colorsFromFamiliarity]
                    const spanStylesToRemove = ['bg-tertiary', 'text-accent']
                    span.classList.add(...spanStylesToAdd)
                    span.classList.remove(...spanStylesToRemove)
                }) 
            }
            #wrapCurrentSelection(phraseNodes) {  
                const form = this.#insertForm({
                    insert: this.#createForm(),
                    before: phraseNodes.at(0)
                })
                const container = this.#createContainer(phraseNodes)
                const phrase = this.#wrapNodes([form, ...phraseNodes], container)
                if(phrase) {
                    this.#markSelection(phrase) 
                    this.#styleNodes(phrase)
                    this.#styleSpans(phrase)
                    this.selectedPhraseData = this.#retrieveData(phraseNodes)                    
                }
            }
            #retrieveData(phraseNodes) {
                const value = this.#getSpans(phraseNodes)
                    .map(span => span.innerText)
                    .join(' ')
                    
                const id = [...phraseNodes]
                    .at(0)
                    ?.dataset.id
                    ?? 0
                return {
                    value,
                    id
                }
            }
            #wrapNodes(content, wrapper) {
                return util.wrap({
                    wrapper: wrapper,
                    content: content
                })
            }
            #createForm() {
                return util.define({
                    type: 'div',
                    classList: ['cursor-default', 'z-2', 'absolute', 'inline', 'left-2', 'top-10'],
                    id: 'selected-phrase-form'
                })
            }
            #insertForm(config) {
                return util.insertBefore({
                    insert: config.insert,
                    before: config.before
                })
            }
            #createContainer() {
                return util.define({
                    type: 'div',
                    classList: ['inline-flex', 'relative', 'rounded', 'text-lg', 'gap-1', 'cursor-pointer', 'border', 'border-2', 'border-secondary', 'bg-tertiary']
                })
            }
            #styleNodes(phrase) {
                const phraseContent = [...phrase.children]
                phraseContent.forEach(node => node.classList.remove('border', 'border-2'))
            }
            #styleSpans(container) {
                const spanStylesToAdd = ['bg-tertiary', 'text-accent']
                const spanStylesToRemove = ['bg-accent', 'bg-accent/80', 'bg-accent/60', 'bg-accent/40', 'bg-primary', 'text-primary']
                const spans = [...container.children]
                    .flatMap(child => [...child.children])
                    .filter(grandchild => grandchild instanceof HTMLSpanElement)
                spans.forEach(span => {
                    span.classList.add(...spanStylesToAdd)
                    span.classList.remove(...spanStylesToRemove)
                })
            }
            #unwrap(container) {
                const children = util.unwrap(container)                
                children.at(0).remove()
                return children.slice(1);
            }
            #getTranslationFormId(phraseNodes) {
                return parseInt(phraseNodes
                    .at(0)
                    ?.dataset.id)
            }
            #closeAllTranslationForms() {                
                [...htmx.findAll('div')]
                    .filter(element => element.id.includes('translation-form-container-'))
                    .forEach(form => form.innerHTML = '')
            }
            #markSelection(container) {
                container.classList.add('selected-phrase')
            }
            #unmarkSelection(container) {
                container.classList.remove('selected-phrase')
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

            #getSelectedPhrase() {
                return htmx.find('.selected-phrase')
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
            #emitEvent() {                
                const event = new Event('selectphrase')
                document.dispatchEvent(event)
            }
            #destroySelector(message) {
                // if(message) {
                //    error('destroying selector: ' + message)
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



