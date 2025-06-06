@import lule.dictionary.controller.imports.dto.DictionaryModel

@param DictionaryModel dictionaryModel

@template.layout.base(content = @`
    @template.importPageContent(dictionaryModel = dictionaryModel)
    <script>
        class ImportPage extends HTMLElement {
            static observedAttributes = ['selected-word-id'];
            constructor() {
                super();
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
                } else {
                    console.error('Element not found: target-word-' + id);
                }
            }

            onWordClick(id) {;
                this.setAttribute('selected-word-id', id);
            }

            connectedCallback() {
                this.setAttribute('selected-word-id', 0);
                this.highlightWord(0);
            }

            attributeChangedCallback(name, oldValue, newValue) {
                if (name === 'selected-word-id') {
                    this.highlightWord(parseInt(newValue));
                }
            }
        }
        if (!customElements.get('import-page')) {
            customElements.define('import-page', ImportPage);
        }
    </script>
    <import-page selectedWordId="${dictionaryModel.selectedWordId()}"></import-page>
`)

