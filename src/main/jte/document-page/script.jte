@param int selectedWordId
<script
class="top-8 top-6 top-10 right-4 bottom-12 bottom-10">
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
                    console.log('NE overflow: adjusting position');
                    this.#adjustPosition({
                        form: form,
                        top: 8,
                        right: 4
                    });
                }
            }
            #handleNorthWestOverflow(form) {
                if (this.#isOverflowLeft(form) && !this.#isOverflowBottom(form)) {
                    console.log('NW overflow: adjusting position');
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
                    console.log('SE overflow: adjusting position');
                    this.#adjustPosition({
                        form: form,
                        bottom: 10,
                        right: 4
                    });
                }
            }
            #handleSouthWestOverflow(form) {
                if (this.#isOverflowLeft(form) && this.#isOverflowBottom(form)) {
                    console.log('SW overflow: adjusting position');
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
                    console.log('S overflow: adjusting position');
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

    if (!customElements.get('word-highlighter')) {
        class WordHighlighter extends HTMLElement {

            static observedAttributes = ['selected-word-id'];
            familiarities = ['unknown', 'recognized', 'familiar', 'known', 'ignored']

            constructor() {
                super();
            }

            connectedCallback() {
                this.setAttribute('selected-word-id', 0);
                this.highlightWord(0);
            }

            attributeChangedCallback(name, oldValue, newValue) {
                if (name === 'selected-word-id' && oldValue !== newValue) {
                    const wordData = this.highlightWord(parseInt(newValue));
                    console.log(wordData.wordElement)
                    if(wordData.wordElement) {
                        const selectedWordFamiliarity = this.familiarities.find(familiarity => wordData.wordElement.classList.contains(familiarity))
                        if(selectedWordFamiliarity) {
                            this.highlightWords(wordData.id, selectedWordFamiliarity)
                        }
                    }
                }
            }

            updateWordId(id) {
                this.#updateId(id)
            }

            highlightWords(id, selectedWordFamiliarity) {
                console.log('calling ya')
                this.#markWordFamiliarities(id, selectedWordFamiliarity)
                this.#highlightMarkedWords()
            }

            #markWordFamiliarities(id, selectedWordFamiliarity) {
                const selectedWordValue = htmx.find('#target-word-raw-' + id).innerText.replace(/[^\p{L}\p{N}]/gu, '').toLowerCase();    
                htmx.findAll('.word-' + selectedWordValue).forEach(word => {
                    this.#updateFamiliarity(word, selectedWordFamiliarity);
                })
            }

            #highlightMarkedWords() {
                htmx.findAll('.target-word').forEach(word => {
                    const familiarity = this.familiarities.find(familiarity => word.classList.contains(familiarity))
                    if(familiarity)
                        this.#updateBgColor(word, familiarity)
                })
            }

            highlightWord(id) {
                this.#closeAllTranslationForms();
                this.#resetHighlights({
                    bgColorToRemove: 'bg-tertiary',
                    textColorToRemove: 'text-neutral',
                    textColorToAdd: 'text-primary',
                    bgColors: ['bg-accent', 'bg-accent/80', 'bg-accent/60', 'bg-accent/40']
                })
                const wordElement = htmx.find('#target-word-raw-' + id);
                if (wordElement) {
                    this.#adjustColors({
                        element: wordElement,
                        textColorToRemove: 'text-primary',
                        bgColorToAdd: 'bg-tertiary',
                        textColorToAdd: 'text-neutral'
                    });
                }
                return {id, wordElement}
            }

            #resetHighlights(config) {
                document.querySelectorAll('[id^="target-word-"]').forEach(word => {
                    if (word.classList.contains(config.bgColorToRemove)) {
                        word.classList.remove(config.bgColorToRemove);
                    }
                    if (config.bgColors.some(color => word.classList.contains(color))) {

                        word.classList.remove(config.textColorToRemove);
                        word.classList.add(config.textColorToAdd);
                    }
                });
            }

            #updateFamiliarity(word, familiarity) {
                switch(familiarity) {
                    case 'unknown':
                        this.#updateClasses({
                            element: word,
                            classToAdd: 'unknown',
                            classesToRemove: this.familiarities
                        });
                        break;

                    case 'recognized':
                        this.#updateClasses({
                            element: word,
                            classToAdd: 'recognized',
                            classesToRemove: this.familiarities
                        });
                        break;

                    case 'familiar':
                        this.#updateClasses({
                            element: word,
                            classToAdd: 'familiar',
                            classesToRemove: this.familiarities
                        });
                        break;

                    case 'known':
                        this.#updateClasses({
                            element: word,
                            classToAdd: 'known',
                            classesToRemove: this.familiarities
                        });
                        break;

                    case 'ignored':
                        this.#updateClasses({
                            element: word,
                            classToAdd: 'ignored',
                            classesToRemove: this.familiarities
                        });
                        break;

                    default:
                        this.#updateClasses({
                            element: word,
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

            #adjustColors(config) {
                if(config.element.classList.contains(config.textColorToRemove)) {
                    config.element.classList.remove(config.textColorToRemove);
                }
                config.element.classList.add(config.bgColorToAdd, config.textColorToAdd);
            }

            #updateId(id) {
                this.setAttribute('selected-word-id', id);
            }
        }
        customElements.define('word-highlighter', WordHighlighter);
    }
</script>
<import-page selected-word-id="${selectedWordId}"></import-page>
<word-highlighter selected-word-id="${selectedWordId}"></word-highlighter>



