@param int selectedWordId
@param int documentId
@raw
<script class="
top-8 top-6 top-10 right-4 bottom-12 bottom-10 
border-b-secondary border-l-secondary border-t-secondary border-r-secondary border-l-transparent border-r-transparent border
border-l-2 border-r-2 border-t-2 border-b-2
px-0.5 pl-0.5 pr-0.5 w-[2px] min-w-[2px]">
    //ABOVE styles used here solely for tailwind live reload server to pick up

    if (!customElements.get('form-position-adjuster')) {
        class FormPositionAdjuster extends HTMLElement {

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
            }
            updateSelectedFormPosition() {
                const form = util.findByData({ key: 'is-selected-form', value: true })
                this.#handleOverflow(form);
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
                if(!form) {
                    return
                }
                if (this.#isOverflowRight(form) && !this.#isOverflowBottom(form)) {
                    this.#adjustPosition({
                        form: form,
                        top: 8,
                        right: 4
                    });
                    // console.log('overflow: NE');
                }
            }
            #handleNorthWestOverflow(form) {
                if(!form) {
                    return
                }
                if (this.#isOverflowLeft(form) && !this.#isOverflowBottom(form)) {
                    this.#adjustPosition({
                        form: form,
                        top: 0,
                        bottom: 0,
                        left: 0,
                        right: 0
                    });
                    // console.log('overflow: NW');
                }
            }
            #handleSouthEastOverflow(form) {
                if(!form) {
                    return
                }
                if (this.#isOverflowRight(form) && this.#isOverflowBottom(form)) {
                    this.#adjustPosition({
                        form: form,
                        bottom: 10,
                        right: 4
                    });
                    // console.log('overflow: SE');
                }
            }
            #handleSouthWestOverflow(form) {
                if(!form) {
                    return
                }
                if (this.#isOverflowLeft(form) && this.#isOverflowBottom(form)) {
                    this.#adjustPosition({
                        form: form,
                        top: 0,
                        bottom: 0,
                        left: 0,
                        right: 0
                    });
                    // console.log('overflow: SW');
                }
            }

            #handleSouthOverflow(form) {
                if(!form) {
                    return
                }
                if (!this.#isOverflowLeft(form) && !this.#isOverflowRight(form) && this.#isOverflowBottom(form)) {
                    this.#adjustPosition({
                        form: form,
                        bottom: 10
                    });
                    // console.log('overflow: S');
                }
            }

            #isOverflowLeft(form) {
                if(!form) {
                    return
                }
                return form.getBoundingClientRect().left > window.innerWidth;
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
                if(!form) {
                    return
                }
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
                if(!config) {
                    return
                }
                if(!config.form) {
                    return
                }
                const positionClasses = Array.from(config.form.classList).filter(cls =>
                    cls.match(/^(right-|left-|top-|bottom-|-right-|-left-|-top-|-bottom-)\d+$/)
                );
                positionClasses.forEach(cls => config.form.classList.remove(cls));
                
            }
            #setDefaultPositions(config) {
                this.#adjustPosition(config)
            }
        }

        customElements.define('form-position-adjuster', FormPositionAdjuster);
    }
    //TODO make this a word highlighter, phrase highlighter will select and highlight phrases in isolation
    if (!customElements.get('word-highlighter')) {
        class WordHighlighter extends HTMLElement {

            static observedAttributes = ['selected-word-id'];
            familiarities = ['unknown', 'recognized', 'familiar', 'known', 'ignored']

            constructor() {
                super();
            }

            connectedCallback() {
                this.setAttribute('selected-word-id', 0);
            }

            attributeChangedCallback(name, oldValue, newValue) {
                if (name === 'selected-word-id') {
                    const formContainers = util.findAllByData({
                        key: 'is-translation-form-container',
                        value: 'true'
                    })
                    this.#closeAllTranslationForms(formContainers)                    
                    this.#removeAllSelections()
                    this.#highlightSelectedWord()
                }
            }
            async #removeAllSelections() {
                this.fadeLastSelectedWord();
                await customElements.whenDefined('phrase-selector');
                const phraseSelector = htmx.find('phrase-selector');
                if (phraseSelector) {
                    phraseSelector.fadeLastSelectedPhrase()
                    phraseSelector.unwrapUnsavedSelections();
                }
                await customElements.whenDefined('phrase-highlighter');
                const phraseHighlighter = htmx.find('phrase-highlighter')
                if(phraseHighlighter) {                    
                    phraseHighlighter.highlightAllSavedPhrases()
                }
            }
            #closeAllTranslationForms(forms) {
                forms.forEach(form => util.removeInnerHTML(form))
            }
            fadeLastSelectedWord() {
                const lastSelectedWord = this.#getSelectedWord();                                                                                    
                if(lastSelectedWord) {
                    if(this.#isWord(lastSelectedWord)) {
                        const familiarity = lastSelectedWord.dataset.familiarity
                        this.#updateColors({
                            node: lastSelectedWord,
                            toRemove: ['bg-tertiary', 'text-neutral'],
                            toAdd: this.#getColorsFromFamiliarity(familiarity)
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

            highlightWords(targetWord, familiarity) {
                const nonSelectedWords = util.findAllByData({ key: 'subtype', value: 'word-span' })
                    .filter(selectable => selectable.dataset.isWordSelected === 'false')
                    .filter(span => span.dataset.value === targetWord)              
                const selectedWords = util.findAllByData({ key: 'subtype', value: 'word-span' })
                    .filter(selectable => selectable.dataset.isWordSelected === 'true')
                    .filter(span => span.dataset.value === targetWord)                
                
                this.#updateFamiliarities([...nonSelectedWords, ...selectedWords], familiarity)                                           
                nonSelectedWords.forEach(selectable => {                    
                    if(this.#isWord(selectable)) {
                        this.#updateColors({
                            node: selectable,
                            toRemove: ['bg-accent', 'bg-accent/80', 'bg-accent/60', 'bg-accent/40', 'bg-primary', 'text-primary'],
                            toAdd: this.#getColorsFromFamiliarity(familiarity)   
                        })
                    }
                })
            }
            #updateFamiliarities(selectables, familiarity) {                
                selectables.forEach(selectable => { selectable.dataset.familiarity = familiarity })    
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
                return selectable.dataset.type === 'word';
            }

            #updateId(id) {
                this.setAttribute('selected-word-id', id);
            }
        }
        customElements.define('word-highlighter', WordHighlighter);
    }
    if (!customElements.get('phrase-highlighter')) {
        class PhraseHighlighter extends HTMLElement {
            highlightPhrases(phrase, familiarity) {                
                if(!this.#isPhrase(phrase)) {
                    return
                }
                this.highlightAllSavedPhrases()
                this.highlightNewPhrases(phrase, familiarity)
            }

            highlightNonSelectedSavedPhrases() {
                const savedPhrases = util.findAllByData({ key: 'is-phrase-saved', value: 'true' });                          
                savedPhrases
                .filter(phrase => phrase.dataset.isPhraseSelected === 'false')
                .forEach(phrase => {
                    if(phrase.id === 'selected-phrase') {
                        phrase.id = 'selectable-' + phrase.firstElementChild.id.slice(27)
                        phrase.firstElementChild.dataset.isSelectedForm = 'false'

                    }
                    util.replaceClasses({
                        node: phrase,
                        toRemove: ['bg-accent', 'bg-accent/80', 'bg-accent/60', 'bg-accent/40', 'bg-primary', 'text-primary', 'bg-tertiary'],
                        toAdd: [...this.#getColorsFromFamiliarity(phrase.dataset.familiarity)]
                    })                    
                    Array.from(phrase.children)
                    .filter(node => node.id.toString().includes('word-'))
                    .forEach(node => {
                        util.replaceClasses({
                            node: node,
                            toRemove: ['border', 'border-2', 'border-transparent'],
                            toAdd: ['pointer-events-none']
                        })                    
                        Array.from(node.children)
                            .filter(child => child instanceof HTMLSpanElement)
                            .forEach(span => {
                                util.replaceClasses({
                                    node: span,
                                    toRemove: ['bg-accent', 'bg-accent/80', 'bg-accent/60', 'bg-accent/40', 'bg-primary', 'text-primary', 'text-neutral', 'bg-tertiary'],
                                    toAdd: [...this.#getTextColor(phrase.dataset.familiarity)]
                                })
                            })
                    })   
                })
            }
            highlightAllSavedPhrases() {
                const savedPhrases = util.findAllByData({ key: 'is-phrase-saved', value: 'true' });                                              
                savedPhrases
                .forEach(phrase => {
                    if(phrase.id === 'selected-phrase') {
                        phrase.id = 'selectable-' + phrase.firstElementChild.id.slice(27)
                        phrase.firstElementChild.dataset.isSelectedForm = 'false'

                    }
                    util.replaceClasses({
                        node: phrase,
                        toRemove: ['bg-accent', 'bg-accent/80', 'bg-accent/60', 'bg-accent/40', 'bg-primary', 'text-primary', 'bg-tertiary'],
                        toAdd: [...this.#getColorsFromFamiliarity(phrase.dataset.familiarity)]
                    })                    
                    Array.from(phrase.children)
                    .filter(node => node.id.toString().includes('word-'))
                    .forEach(node => {
                        util.replaceClasses({
                            node: node,
                            toRemove: ['border', 'border-2', 'border-transparent'],
                            toAdd: ['pointer-events-none']
                        })                    
                        Array.from(node.children)
                            .filter(child => child instanceof HTMLSpanElement)
                            .forEach(span => {
                                util.replaceClasses({
                                    node: span,
                                    toRemove: ['bg-accent', 'bg-accent/80', 'bg-accent/60', 'bg-accent/40', 'bg-primary', 'text-primary', 'text-neutral', 'bg-tertiary'],
                                    toAdd: [...this.#getTextColor(phrase.dataset.familiarity)]
                                })
                            })
                    })   
                })
                
            }
            highlightNewPhrases(phrase,  familiarity) {
                if(!this.#isPhrase(phrase)) {
                    return
                } 
                const selectables = util.findAllByData({ key: 'is-selectable', value: 'true' })
                    .flatMap(selectable => {
                        if(selectable.dataset.type === 'word') {
                            return [...selectable.children].filter(child => child instanceof HTMLSpanElement)
                        }
                        return selectable
                    })
                    
                const result = util.catchErr(() => util.findAllByText({
                    text: phrase,
                    nodes: selectables
                }))
                if(result.ok) {
                    if(!result.length) {
                        return
                    }                    
                    const phraseNodeArr = result.value
                        .map(spans => spans.map(span => span.parentElement))
                                            
                    const selectedPhraseNodes = phraseNodeArr
                        .filter(divs => divs.every(div => div.dataset.isWrapped === 'true'))
                        .at(0)
                        
                    this.#updateWrapperFamiliarity(selectedPhraseNodes.at(0).parentElement, familiarity.toLowerCase())
                    this.#updateContentFamiliarity(selectedPhraseNodes, familiarity.toLowerCase())

                    const nonSelectedPhraseNodeArr = phraseNodeArr
                        .filter(divs => divs.some(div => div.dataset.isWrapped === 'false'))      
                    nonSelectedPhraseNodeArr.forEach(arr => {
                        const form = this.#insertForm({
                            insert: this.#createForm(),
                            before: arr.at(0)
                        })
                        const result = util.catchErr(() => util.wrap({ wrapper: this.#createWrapper(familiarity), content: [form, ...arr] }))
                        if(result.ok) {
                            const wrapped = result.value
                            this.#updateWrapperFamiliarity(wrapped.wrapper, familiarity.toLowerCase())
                            this.#updateContentFamiliarity(wrapped.content, familiarity.toLowerCase())
                            this.#updateContentStyles(wrapped.content, familiarity)
                        }
                        else {
                            console.error(result.err)
                        }
                    })
                    return selectedPhraseNodes
                }
                else {
                    console.error(result.err)
                }
            }
            #isPhrase(phrase) {
                return phrase.split(" ").length > 1
            }
            markPhrasesAsSaved(familiarity, sourceWords) {
                const nonSavedPhrases = util.findAllByData({ key: 'is-phrase-saved', value: 'false' })             
                nonSavedPhrases.forEach(phrase => {                     
                    const formContainer = phrase.firstElementChild
                    const nestedFormContainer = phrase.firstElementChild.nextSibling.firstElementChild
                    if(nestedFormContainer) {
                        const formId = nestedFormContainer.id
                        const phraseId = nestedFormContainer.id.slice(27)
                        const targetWord = Array.from(phrase.children)
                            .filter(child => child.dataset.type === 'word')
                            .flatMap(word => [...word.children])
                            .filter(text => text instanceof HTMLSpanElement)
                            .map(span => span.innerText)
                            .join(" ")
                        nestedFormContainer.remove()  
                        this.#updatePhraseHxAttributes(phrase, formId, phraseId, targetWord, sourceWords, familiarity)                      
                        this.#updatePhraseData(phrase, familiarity)
                        this.#updateFormContainerId(formContainer, formId)
                        this.#updateFormContainerStyles(formContainer)                           
                    }
                })
            }
            #updateFormContainerId(form, id) {
                form.id = id  
            }
            #updateFormContainerStyles(form) {
                form.classList.remove("pointer-events-none");
            }
            #updatePhraseData(phrase, familiarity) {
                phrase.dataset.isPhraseSaved = 'true' 
                phrase.dataset.familiarity = familiarity
            }
            @endraw
            #updatePhraseHxAttributes(phrase, formId, phraseId, targetWord, sourceWords, familiarity) {                                
                util.setHxAttributes({
                    element: phrase,
                    method: { key: 'hx-get', value: '/translations/find' },
                    target: '#' + formId,
                    swap: 'innerHTML',
                    trigger: 'click',
                    vals: {
                        documentId: "${documentId}",
                        targetWord: targetWord
                            .toLowerCase()
                            .replaceAll(/[^\p{L}\d\s]/gu, ''),
                        sourceWords: sourceWords
                            .split("|"),
                        selectedWordId: phraseId.toString(),
                        isFound: "true",
                        isPhrase: "true"
                    },
    @raw
                    beforeRequest: `htmx.find('phrase-selector').handlePhraseClick(${phraseId});`
                })
            }
            #updateWrapperFamiliarity(wrapper, familiarity) {
                wrapper.dataset.familiarity = familiarity
            }
            #updateContentFamiliarity(content, familiarity) {
                content.forEach(node => node.dataset.familiarity = familiarity)
            }
            #updateContentStyles(content, familiarity) {
                content.forEach(node => {
                    util.replaceClasses({
                        node: node,
                        toRemove: ['border', 'border-2', 'border-transparent'],
                        toAdd: ['pointer-events-none']
                    })                    
                    Array.from(node.children)
                        .filter(child => child instanceof HTMLSpanElement)
                        .forEach(span => {
                            util.replaceClasses({
                                node: span,
                                toRemove: ['bg-accent', 'bg-accent/80', 'bg-accent/60', 'bg-accent/40', 'bg-primary', 'text-primary', 'text-neutral', 'bg-tertiary'],
                                toAdd: [...this.#getColorsFromFamiliarity(familiarity)]
                            })
                        })
                })
            }
            #createForm() {
                return util.define({
                    type: 'div',
                    classList: ['cursor-default', 'z-2', 'absolute', 'inline', 'left-2', 'top-10'],
                    data: [
                        { key: 'is-translation-form-container', value: 'true' }
                    ]
                })
            }
            #insertForm(config) {
                return util.insertBefore({
                    insert: config.insert,
                    before: config.before
                })
            }
            #createWrapper(familiarity) {
                return util.define({
                    type: 'div',
                    classList: [
                        ...this.#getColorsFromFamiliarity(familiarity),
                        'inline-flex', 'relative', 'rounded', 'text-lg', 'gap-1', 'cursor-pointer', 'border', 'border-2', 'border-secondary'
                    ],
                    data: [
                        { key: 'is-phrase-saved', value: 'false' }
                    ]
                })
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
            #getTextColor(familiarity) {
                switch(familiarity) {
                    case 'unknown':
                        return ['text-primary']

                    case 'recognized':
                        return ['text-primary']

                    case 'familiar':
                        return ['text-primary']

                    case 'known':
                        return ['text-neutral']

                    default:
                        return ['text-neutral']
                }
            }
        }
        customElements.define('phrase-highlighter', PhraseHighlighter);
    }
    if (!customElements.get('phrase-selector')) {
        class PhraseSelector extends HTMLElement {
            selectedPhraseData = {
                value: '',
                id: 0,
            }
            constructor() {
                super()
                this.#initializeListener()
            }
            handlePhraseClick(id) {
                const formContainers = util.findAllByData({ key: 'is-translation-form-container', value: 'true' })
                this.#closeAllTranslationForms(formContainers)
                htmx.find('word-highlighter').fadeLastSelectedWord()
                this.fadeLastSelectedPhrase()
                this.#highlightCurrentSelection(id)
            }

            fadeLastSelectedPhrase() {
                const phraseToFade = util.findByData({ key: 'is-phrase-selected', value: 'true' })
                if(phraseToFade) {
                    util.replaceClasses({
                        node: phraseToFade,
                        toRemove: ['bg-tertiary', 'text-accent'],
                        toAdd: this.#getColorsFromFamiliarity(phraseToFade.dataset.familiarity)
                    })
                    phraseToFade.dataset.isPhraseSelected = 'false'

                }
            }
            #highlightCurrentSelection(id) {
                const phrase = htmx.find('#selectable-' + id)
                if(!phrase) {
                    return
                }            
                phrase.dataset.isPhraseSelected = 'true'
                
                util.replaceClasses({
                    node: phrase,
                    toRemove: ['bg-tertiary', 'text-accent', 'bg-accent', 'bg-accent/80', 'bg-accent/60', 'bg-accent/40', 'bg-primary', 'text-primary'],
                    toAdd: ['bg-tertiary', 'text-accent']
                })
            }
            #selectPhrase(phraseHighlighter) {
                const phraseNodes = this.#getPhraseNodes();     
                const result = util.catchErr(() => this.#validate(phraseNodes))     

                if(result.ok) {    
                    const formContainers = util.findAllByData({ key: 'is-translation-form-container', value: 'true' })
                    this.#closeAllTranslationForms(formContainers)                                    
                    this.#removeAllSelections(htmx.find('word-highlighter'))
                    this.#wrapCurrentSelection(phraseNodes)
                    this.#emitEvent()
                    this.#destroySelection()   
                    phraseHighlighter.highlightPhrases(
                        this.selectedPhraseData.value, 
                        'unknown'
                    )
                }
            }
            #removeAllSelections(wordHighlighter) {
                this.unwrapUnsavedSelections()
                wordHighlighter.fadeLastSelectedWord()
            }
            unwrapUnsavedSelections() {
                const phraseWrappers = this.#getHighlightedPhrases()                                
                if(phraseWrappers) {
                    phraseWrappers.forEach(wrapper => {
                        if(!wrapper) {                            
                            return
                        }
                        if(wrapper.firstElementChild) {
                            wrapper.firstElementChild.dataset.isSelectedForm = 'false'
                        }
                        
                        if(wrapper.dataset.isPhraseSaved === 'false') {
                            if(!wrapper.children.length) {
                                return
                            }                            
                            const phraseNodes = this.#unwrap(wrapper)      
                            phraseNodes.forEach(node => htmx.process(node))                                                 
                            this.#unstyleNodes(phraseNodes) 
                        }
                    })
                }
            }
            #unstyleNodes(phraseNodes) {
                phraseNodes.forEach(node => {
                    util.replaceClasses({ 
                        node: node, 
                        toRemove: 'pointer-events-none', 
                        toAdd:  ['border', 'border-2', 'border-transparent']
                    })
                    const spans = [...node.children].filter(grandchild => grandchild instanceof HTMLSpanElement)
                    spans.forEach(span => {
                        util.replaceClasses({
                            node: span,
                            toRemove: ['bg-tertiary', 'text-accent', 'bg-accent', 'bg-accent/80', 'bg-accent/60', 'bg-accent/40', 'bg-primary', 'text-primary'],
                            toAdd: this.#getColorsFromFamiliarity(span.dataset.familiarity)
                        })
                    })   
                })
            }
            #wrapCurrentSelection(phraseNodes) {
                const form = this.#insertForm({
                    insert: this.#createForm(`translation-form-container-${phraseNodes?.at(0)?.dataset?.id}` ?? 0),
                    before: phraseNodes.at(0)
                })
                
                const container = this.#createContainer()
                const wrapped = util.wrap({ wrapper: container, content: [form, ...phraseNodes] })
                if(wrapped) {                    
                    wrapped.content.forEach(node => htmx.process(node))
                    
                    const wrappedPhraseNodes = wrapped.content.slice(1)                              
                    this.#styleNodes(wrappedPhraseNodes)
                    this.selectedPhraseData = this.#retrieveData(wrappedPhraseNodes)     
                }
            }
            #retrieveData(phraseNodes) {                
                const value = phraseNodes
                    .flatMap(node => [...node.children])
                    .filter(child => child instanceof HTMLSpanElement)
                    .map(span => span.innerText)
                    .join(' ')                    
                const id = phraseNodes.at(0)?.dataset.id ?? 0                                                
                return { value, id }
            }
            #validate(phraseNodes) {  
                if(!phraseNodes) {
                    this.#destroySelection()
                    throw new Error('phrase nodes cant be undefined')
                }                              
                if(!phraseNodes.length) {
                    this.#destroySelection()
                    throw new Error('phrase nodes cant be empty')
                }
                if(phraseNodes.length > 5) {
                    this.#destroySelection()
                    throw new Error('selection too long')
                }
                if(phraseNodes.some(node => node?.dataset.isWordSelected === 'true')) {
                    this.#destroySelection()
                    throw new Error('cannot create phrase from selected word')
                }
                if(!phraseNodes.every(node => node?.dataset.isWordSelectable === 'true')) {
                    this.#destroySelection()
                    throw new Error('all phrase parent nodes must be selectable')
                }
            }
            #createForm(id) {
                return util.define({
                    type: 'div',
                    classList: ['cursor-default', 'z-2', 'absolute', 'inline', 'left-2', 'top-10'],
                    id: id,
                    data: [
                        { key: 'is-selected-form', value: 'true' },
                        { key: 'is-translation-form-container', value: 'true' },
                        { key: 'id', value: id }
                    ]
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
                    classList: [
                        'inline-flex', 'relative', 'rounded', 'text-lg', 'gap-1', 'cursor-pointer', 'border', 'border-2', 'border-secondary', 'bg-tertiary'
                    ],
                    id: 'selected-phrase',
                    data: [
                        { key: 'is-phrase-saved', value: 'false' },
                        { key: 'is-phrase-selected', value: 'true' }
                    ]
                })
            }
            #styleNodes(phraseNodes) {
                phraseNodes.forEach(node => {
                    util.replaceClasses({ 
                        node: node, 
                        toRemove: ['border', 'border-2', 'border-transparent'], 
                        toAdd:  'pointer-events-none'
                    })

                    const spans = Array.from(node.children)
                        .filter(child => child instanceof HTMLSpanElement)
                    spans.forEach(span => util.replaceClasses({
                        node: span,
                        toRemove: ['bg-accent', 'bg-accent/80', 'bg-accent/60', 'bg-accent/40', 'bg-primary', 'text-primary'],
                        toAdd: ['bg-tertiary', 'text-accent']
                    }))
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
            #closeAllTranslationForms(forms) {
                forms.forEach(form => util.removeInnerHTML(form))
            }
            #getHighlightedPhrases() {
                const selectedPhrase = htmx.find('#selected-phrase')
                const highlightedPhrases =  util.findAllByData({ key: 'is-saved', value: 'false' })
                return [selectedPhrase, ...highlightedPhrases]
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
            #destroySelection() {
                window.getSelection().removeAllRanges()
            }

            #initializeListener() {
                htmx.find('#page-content').addEventListener('mouseup', () =>  this.#selectPhrase(
                    htmx.find('phrase-highlighter'), 
                    
                ))
                htmx.find('#page-content').addEventListener('htmx:afterSwap', e => {
                    htmx.find('form-position-adjuster').updateSelectedFormPosition(e.target)                    
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
                return Array.from(
                    { length: this.#getFocusId() - this.#getAnchorId() + 1 },
                    (_, i) => htmx.find(`#word-${i + this.#getAnchorId()}`)
                )
                .filter(node => node !== null)
            }

            #getPhraseText(phraseNodes) {
                return phraseNodes
                            .filter(node => node !== null)
                            .map(node => node.innerText).join(" ")
            }

            #getAnchorId() {                
                return parseInt(window.getSelection()?.anchorNode?.parentElement?.parentElement?.dataset?.id)
            }

            #getFocusId() {
                return parseInt(window.getSelection()?.focusNode?.parentElement?.parentElement?.dataset?.id)
            }

        }
        customElements.define('phrase-selector', PhraseSelector);
    }

@endraw
</script>
<form-position-adjuster selected-word-id="0"></form-position-adjuster>




