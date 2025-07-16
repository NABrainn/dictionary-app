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
                this.highlightWord(0);
            }

            closeAllTranslationForms() {
                document.querySelectorAll('[id^="translation-form-"]').forEach(form => {
                    form.innerHTML = '';
                });
            }

            resetHighlights() {
                document.querySelectorAll('[id^="target-word-"]').forEach(word => {
                    if (word.classList.contains('bg-tertiary')) {
                        word.classList.remove('bg-tertiary');
                    }
                    if (
                        word.classList.contains('bg-accent') ||
                        word.classList.contains('bg-accent/80') ||
                        word.classList.contains('bg-accent/60') ||
                        word.classList.contains('bg-accent/40')) {

                        word.classList.remove('text-neutral');
                        word.classList.add('text-primary');
                    }
                });
            }

            highlightWord(id) {
                this.closeAllTranslationForms();
                this.resetHighlights()
                const wordElement = document.getElementById('target-word-' + id);
                if (wordElement) {
                    if(wordElement.classList.contains('text-primary')) {
                        wordElement.classList.remove('text-primary');
                    }
                    wordElement.classList.add('bg-tertiary', 'text-neutral');
                }
            }

            onWordClickBefore(id) {
                this.#updateId(id)
            }

            updatePositions(id) {
                this.#updateId(id)
                const form = this.#getTranslationForm();
                this.#handleOverflow(form);
                this.#setInputPositions();
            }

            #setInputPositions() {
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

            attributeChangedCallback(name, oldValue, newValue) {
                if (name === 'selected-word-id' && oldValue !== newValue) {
                    this.highlightWord(parseInt(newValue));
                }
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
                console.log(this.positions)
                
            }

            #handleNorthEastOverflow(form) {
                if (this.#isOverflowRight(form) && !this.#isOverflowBottom(form)) {
                    const currentRight = Array.from(form.classList).find(cls => cls.startsWith('right-') || cls.startsWith('-right-'));
                    const currentTop = Array.from(form.classList).find(cls => cls.startsWith('top-') || cls.startsWith('-top-'));
                    if (currentRight === 'right-4' && currentTop === 'top-8') {
                        console.log('NE overflow: already positioned at right-4 top-8, skipping adjustment');
                        return;
                    }
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
                    const currentLeft = Array.from(form.classList).find(cls => cls.startsWith('left-') || cls.startsWith('-left-'));
                    const currentTop = Array.from(form.classList).find(cls => cls.startsWith('top-') || cls.startsWith('-top-'));
                    if (currentLeft === 'left-0' && currentTop === 'top-0') {
                        console.log('NW overflow: already positioned at left-0 top-0, skipping adjustment');
                        return;
                    }
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
                    const currentRight = Array.from(form.classList).find(cls => cls.startsWith('right-') || cls.startsWith('-right-'));
                    const currentBottom = Array.from(form.classList).find(cls => cls.startsWith('bottom-') || cls.startsWith('-bottom-'));
                    if (currentRight === 'right-4' && currentBottom === 'bottom-10') {
                        console.log('SE overflow: already positioned at right-4 bottom-10, skipping adjustment');
                        return;
                    }
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
                    const currentLeft = Array.from(form.classList).find(cls => cls.startsWith('left-') || cls.startsWith('-left-'));
                    const currentBottom = Array.from(form.classList).find(cls => cls.startsWith('bottom-') || cls.startsWith('-bottom-'));
                    if (currentLeft === 'left-0' && currentBottom === 'bottom-0') {
                        console.log('SW overflow: already positioned at left-0 bottom-0, skipping adjustment');
                        return;
                    }
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
                    const currentBottom = Array.from(form.classList).find(cls => cls.startsWith('bottom-') || cls.startsWith('-bottom-'));
                    if (currentBottom === 'bottom-10') {
                        console.log('S overflow: already positioned at bottom-10, skipping adjustment');
                        return;
                    }
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
                for(const [key, value] of Object.entries(positions)) {
                    if(value !== '') {
                        config.form.classList.add(value)
                    }
                    this.positions[key] = value
                }
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
</script>
<import-page selected-word-id="${selectedWordId}"></import-page>


