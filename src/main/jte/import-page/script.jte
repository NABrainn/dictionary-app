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
                document.querySelectorAll('.left-' + wordId).forEach((input) => {
                    input.value=this.positions.left
                })
                document.querySelectorAll('.right-' + wordId).forEach((input) => {
                    input.value=this.positions.right
                })
                document.querySelectorAll('.top-' + wordId).forEach((input) => {
                    input.value=this.positions.top
                })
                document.querySelectorAll('.bottom-' + wordId).forEach((input) => {
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
                return document.getElementById('translation-form-container-' + this.getAttribute('selected-word-id'));
            }

            #handleOverflow(form) {
                this.#handleNorthEastOverflow(form)
                this.#handleNorthWestOverflow(form)
                this.#handleSouthEastOverflow(form)
                this.#handleSouthWestOverflow(form)
                this.#handleSouthOverflow(form)
            }

            #handleNorthEastOverflow(form) {
                if(this.#isOverflowRight(form) && !this.#isOverflowBottom(form)) {
                    console.log('NE overflow: ');
                    this.#adjustPosition({
                        form: form,
                        top: 8,
                        right: 4
                    });
                }
            }
            #handleNorthWestOverflow(form) {
                if(this.#isOverflowLeft(form) && !this.#isOverflowBottom(form)) {
                    console.log('NW overflow: ');
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
                if(this.#isOverflowRight(form) && this.#isOverflowBottom(form)) {
                    console.log('SE overflow: ');
                    this.#adjustPosition({
                        form: form,
                        bottom: 10,
                        right: 4
                    });
                }
            }
            #handleSouthWestOverflow(form) {
                if(this.#isOverflowLeft(form) && this.#isOverflowBottom(form)) {
                    console.log('SW overflow');
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
                if(!this.#isOverflowLeft(form) && !this.#isOverflowRight(form) && this.#isOverflowBottom(form)) {
                    console.log('S overflow');
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
                this.#cleanupPositions(config);
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
        }

        customElements.define('import-page', ImportPage);
    }
</script>
<import-page selected-word-id="${selectedWordId}"></import-page>


