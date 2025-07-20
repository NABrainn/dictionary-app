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

            highlightWord(id) {
                this.#closeAllTranslationForms();
                this.#resetHighlights({
                    bgColorToRemove: 'bg-tertiary',
                    textColorToRemove: 'text-neutral',
                    textColorToAdd: 'text-primary',
                    bgColors: ['bg-accent', 'bg-accent/80', 'bg-accent/60', 'bg-accent/40']
                })
                const wordElement = htmx.find('#target-word-' + id);
                if (wordElement) {
                    this.#adjustColors({
                        element: wordElement,
                        textColorToRemove: 'text-primary',
                        bgColorToAdd: 'bg-tertiary',
                        textColorToAdd: 'text-neutral'
                    });
                }
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

            #adjustColors(config) {
                if(config.element.classList.contains(config.textColorToRemove)) {
                    config.element.classList.remove(config.textColorToRemove);
                }
                config.element.classList.add(config.bgColorToAdd, config.textColorToAdd);
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

            constructor() {
                super();
            }

            connectedCallback() {
                this.setAttribute('selected-word-id', 0);
                this.highlightWord(0);
            }

            attributeChangedCallback(name, oldValue, newValue) {
                if (name === 'selected-word-id' && oldValue !== newValue) {
                    this.highlightWord(parseInt(newValue));
                }
            }

            highlightWords(id, familiarity) {
                const classesToRemove = ['bg-accent', 'bg-accent/80', 'bg-accent/60', 'bg-accent/40', 'bg-primary',  'text-neutral', 'text-primary'];
                const selectedWordValue = htmx.find('#target-word-raw-' + id).innerText;
                console.log(selectedWordValue)
                const allWordsByValue = htmx.findAll('.word-' + selectedWordValue).forEach(word => {
                    switch(familiarity) {
                        case 1:
                            console.log('UNKNOWN')
                            if(!word.classList.contains('bg-tertiary')) {
                                word.classList.remove(...classesToRemove)
                                word.classList.add('bg-accent')
                                word.classList.add('text-primary')
                            }
                            break;

                        case 2:
                            if(!word.classList.contains('bg-tertiary')) {
                                console.log('RECOGNIZED')
                                word.classList.remove(...classesToRemove)
                                word.classList.add('bg-accent/80')
                                word.classList.add('text-primary')
                            }
                            break;

                        case 3:
                            if(!word.classList.contains('bg-tertiary')) {
                                console.log('FAMILIAR')
                                word.classList.remove(...classesToRemove)
                                word.classList.add('bg-accent/60')
                                word.classList.add('text-primary')
                            }
                            break;

                        case 4:
                            if(!word.classList.contains('bg-tertiary')) {
                                console.log('KNOWN')
                                word.classList.remove(...classesToRemove)
                                word.classList.add('bg-primary')
                                word.classList.add('text-neutral')
                            }
                            break;

                        case 5:
                            if(!word.classList.contains('bg-tertiary')) {
                                console.log('IGNORED')
                                word.classList.remove(...classesToRemove)
                                word.classList.add('bg-primary')
                                word.classList.add('text-neutral')
                                break;
                            }

                        default:
                            console.log('IGNORED')
                            break;
                    }
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
                const wordElement = htmx.find('#target-word-' + id);
                if (wordElement) {
                    this.#adjustColors({
                        element: wordElement,
                        textColorToRemove: 'text-primary',
                        bgColorToAdd: 'bg-tertiary',
                        textColorToAdd: 'text-neutral'
                    });
                }
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



