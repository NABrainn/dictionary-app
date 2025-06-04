@import lule.dictionary.service.application.ImportData

@param ImportData data
@param int selectedWordId

@template.layout.base(content = @`
    @template.importPageContent(data = data, selectedWordId = selectedWordId)
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
                document.querySelectorAll('[id^="target-word-"]').forEach(word => {
                    word.classList.remove('bg-tertiary');
                });
            }

            highlightWord(id) {
                const wordElement = document.getElementById('target-word-' + id);
                if (wordElement) {
                    wordElement.classList.add('bg-tertiary');
                } else {
                    console.error('Element not found: target-word-' + id);
                }
            }

            onWordClick(id) {
                this.closeAllTranslationForms();
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

    <import-page selectedWordId="${selectedWordId}"></import-page>
`)

