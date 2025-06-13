@import lule.dictionary.controller.catalog.dto.ImportPageModel
@import lule.dictionary.enumeration.Familiarity
@import gg.jte.support.ForSupport
@import org.springframework.security.web.csrf.CsrfToken

@param ImportPageModel importPageModel
@param CsrfToken _csrf
@param boolean authenticated

@template.layout.base(content = @`
    <div id="page-content" class="flex flex-col gap-8 w-3/5 mx-auto">
        <span class="text-neutral font-bold text-4xl">${importPageModel.title()}</span>
        <div class="leading-10">
            @for(var word : ForSupport.of(importPageModel.content()))
                !{var cleanWord = word.get().replaceAll("[^\\p{L}\\p{N}]", "").trim().toLowerCase();}
                @if(importPageModel.translations().get(cleanWord) != null)
                    @if(importPageModel.selectedWordId() == word.getIndex())
                        @if(importPageModel.translations().get(cleanWord).translationDetails().familiarity() == Familiarity.UNKNOWN)
                            <span
                                    class="bg-tertiary bg-accent relative text-neutral px-0.1 py-0.1 cursor-pointer rounded whitespace-nowrap"
                                    id="target-word-${word.getIndex()}"
                                    hx-get="/translations?targetWord=${cleanWord}&importId=${importPageModel.importId()}"
                                    hx-target="#translation-form-${word.getIndex()}"
                                    hx-swap="innerHTML"
                                    hx-vals='{"selectedWordId": "${word.getIndex()}"}'
                                    hx-on::before-request="document.querySelector('import-page').onWordClick(${word.getIndex()})">
                        ${word.get()}<div class="cursor-default z-2 inline absolute -left-4 top-16" id="translation-form-${word.getIndex()}" onclick="event.stopPropagation();"></div></span>
                        @elseif(importPageModel.translations().get(cleanWord).translationDetails().familiarity() == Familiarity.RECOGNIZED)
                            <span
                                    class="bg-tertiary bg-accent/80 relative text-neutral px-0.1 py-0.1 cursor-pointer rounded whitespace-nowrap"
                                    id="target-word-${word.getIndex()}"
                                    hx-get="/translations?targetWord=${cleanWord}&importId=${importPageModel.importId()}"
                                    hx-target="#translation-form-${word.getIndex()}"
                                    hx-swap="innerHTML"
                                    hx-vals='{"selectedWordId": "${word.getIndex()}"}'
                                    hx-on::before-request="document.querySelector('import-page').onWordClick(${word.getIndex()})">
                        ${word.get()}<div class="cursor-default z-2 inline absolute -left-4 top-16" id="translation-form-${word.getIndex()}" onclick="event.stopPropagation();"></div></span>
                        @elseif(importPageModel.translations().get(cleanWord).translationDetails().familiarity() == Familiarity.FAMILIAR)
                            <span
                                    class="bg-tertiary bg-accent/60 relative text-neutral px-0.1 py-0.1 cursor-pointer rounded whitespace-nowrap"
                                    id="target-word-${word.getIndex()}"
                                    hx-get="/translations?targetWord=${cleanWord}&importId=${importPageModel.importId()}"
                                    hx-target="#translation-form-${word.getIndex()}"
                                    hx-swap="innerHTML"
                                    hx-vals='{"selectedWordId": "${word.getIndex()}"}'
                                    hx-on::before-request="document.querySelector('import-page').onWordClick(${word.getIndex()})">
                        ${word.get()}<div class="cursor-default z-2 inline absolute -left-4 top-16" id="translation-form-${word.getIndex()}" onclick="event.stopPropagation();"></div></span>
                        @elseif(importPageModel.translations().get(cleanWord).translationDetails().familiarity() == Familiarity.KNOWN)
                            <span
                                    class="bg-tertiary bg-accent/40 relative text-neutral px-0.1 py-0.1 cursor-pointer rounded whitespace-nowrap"
                                    id="target-word-${word.getIndex()}"
                                    hx-get="/translations?targetWord=${cleanWord}&importId=${importPageModel.importId()}"
                                    hx-target="#translation-form-${word.getIndex()}"
                                    hx-swap="innerHTML"
                                    hx-vals='{"selectedWordId": "${word.getIndex()}"}'
                                    hx-on::before-request="document.querySelector('import-page').onWordClick(${word.getIndex()})">
                        ${word.get()}<div class="cursor-default z-2 inline absolute -left-4 top-16" id="translation-form-${word.getIndex()}" onclick="event.stopPropagation();"></div></span>
                        @else
                            <span
                                    class="bg-tertiary bg-primary relative text-neutral px-0.1 py-0.1 cursor-pointer rounded whitespace-nowrap"
                                    id="target-word-${word.getIndex()}"
                                    hx-get="/translations?targetWord=${cleanWord}&importId=${importPageModel.importId()}"
                                    hx-target="#translation-form-${word.getIndex()}"
                                    hx-swap="innerHTML"
                                    hx-vals='{"selectedWordId": "${word.getIndex()}"}'
                                    hx-on::before-request="document.querySelector('import-page').onWordClick(${word.getIndex()})">
                        ${word.get()}<div class="cursor-default z-2 inline absolute -left-4 top-16" id="translation-form-${word.getIndex()}" onclick="event.stopPropagation();"></div></span>
                        @endif
                    @else
                        @if(importPageModel.translations().get(cleanWord).translationDetails().familiarity() == Familiarity.UNKNOWN)
                            <span
                                    class="bg-accent relative text-primary px-0.1 py-0.1 cursor-pointer rounded whitespace-nowrap"
                                    id="target-word-${word.getIndex()}"
                                    hx-get="/translations?targetWord=${cleanWord}&importId=${importPageModel.importId()}"
                                    hx-target="#translation-form-${word.getIndex()}"
                                    hx-swap="innerHTML"
                                    hx-vals='{"selectedWordId": "${word.getIndex()}"}'
                                    hx-on::before-request="document.querySelector('import-page').onWordClick(${word.getIndex()})">
                        ${word.get()}<div class="cursor-default z-2 inline absolute -left-4 top-16" id="translation-form-${word.getIndex()}" onclick="event.stopPropagation();"></div></span>
                        @elseif(importPageModel.translations().get(cleanWord).translationDetails().familiarity() == Familiarity.RECOGNIZED)
                            <span
                                    class="bg-accent/80 relative text-primary px-0.1 py-0.1 cursor-pointer rounded whitespace-nowrap"
                                    id="target-word-${word.getIndex()}"
                                    hx-get="/translations?targetWord=${cleanWord}&importId=${importPageModel.importId()}"
                                    hx-target="#translation-form-${word.getIndex()}"
                                    hx-swap="innerHTML"
                                    hx-vals='{"selectedWordId": "${word.getIndex()}"}'
                                    hx-on::before-request="document.querySelector('import-page').onWordClick(${word.getIndex()})">
                        ${word.get()}<div class="cursor-default z-2 inline absolute -left-4 top-16" id="translation-form-${word.getIndex()}" onclick="event.stopPropagation();"></div></span>
                        @elseif(importPageModel.translations().get(cleanWord).translationDetails().familiarity() == Familiarity.FAMILIAR)
                            <span
                                    class="bg-accent/60 relative text-primary px-0.1 py-0.1 cursor-pointer rounded whitespace-nowrap"
                                    id="target-word-${word.getIndex()}"
                                    hx-get="/translations?targetWord=${cleanWord}&importId=${importPageModel.importId()}"
                                    hx-target="#translation-form-${word.getIndex()}"
                                    hx-swap="innerHTML"
                                    hx-vals='{"selectedWordId": "${word.getIndex()}"}'
                                    hx-on::before-request="document.querySelector('import-page').onWordClick(${word.getIndex()})">
                        ${word.get()}<div class="cursor-default z-2 inline absolute -left-4 top-16" id="translation-form-${word.getIndex()}" onclick="event.stopPropagation();"></div></span>
                        @elseif(importPageModel.translations().get(cleanWord).translationDetails().familiarity() == Familiarity.KNOWN)
                            <span
                                    class="bg-accent/40 relative text-primary px-0.1 py-0.1 cursor-pointer rounded whitespace-nowrap"
                                    id="target-word-${word.getIndex()}"
                                    hx-get="/translations?targetWord=${cleanWord}&importId=${importPageModel.importId()}"
                                    hx-target="#translation-form-${word.getIndex()}"
                                    hx-swap="innerHTML"
                                    hx-vals='{"selectedWordId": "${word.getIndex()}"}'
                                    hx-on::before-request="document.querySelector('import-page').onWordClick(${word.getIndex()})">
                        ${word.get()}<div class="cursor-default z-2 inline absolute -left-4 top-16" id="translation-form-${word.getIndex()}" onclick="event.stopPropagation();"></div></span>
                        @else
                            <span
                                    class="bg-primary relative text-neutral px-0.1 py-0.1 cursor-pointer rounded whitespace-nowrap"
                                    id="target-word-${word.getIndex()}"
                                    hx-get="/translations?targetWord=${cleanWord}&importId=${importPageModel.importId()}"
                                    hx-target="#translation-form-${word.getIndex()}"
                                    hx-swap="innerHTML"
                                    hx-vals='{"selectedWordId": "${word.getIndex()}"}'
                                    hx-on::before-request="document.querySelector('import-page').onWordClick(${word.getIndex()})">
                        ${word.get()}<div class="cursor-default z-2 inline absolute -left-4 top-16" id="translation-form-${word.getIndex()}" onclick="event.stopPropagation();"></div></span>
                        @endif
                    @endif
                @else
                    @if(importPageModel.selectedWordId() == word.getIndex())
                        <span
                                class="bg-tertiary relative text-neutral px-0.1 py-0.1 cursor-pointer rounded whitespace-nowrap"
                                id="target-word-${word.getIndex()}"
                                hx-get="/translations?targetWord=${cleanWord}&importId=${importPageModel.importId()}"
                                hx-target="#translation-form-${word.getIndex()}"
                                hx-swap="innerHTML"
                                hx-vals='{"selectedWordId": "${word.getIndex()}"}'
                                hx-on::before-request="document.querySelector('import-page').onWordClick(${word.getIndex()})">
                    ${word.get()}<div class="cursor-default z-2 inline absolute -left-4 top-16" id="translation-form-${word.getIndex()}" onclick="event.stopPropagation();"></div></span>
                    @else
                        <span
                                class="bg-primary relative text-neutral px-0.1 py-0.1 cursor-pointer rounded whitespace-nowrap"
                                id="target-word-${word.getIndex()}"
                                hx-get="/translations?targetWord=${cleanWord}&importId=${importPageModel.importId()}"
                                hx-target="#translation-form-${word.getIndex()}"
                                hx-swap="innerHTML"
                                hx-vals='{"selectedWordId": "${word.getIndex()}"}'
                                hx-on::before-request="document.querySelector('import-page').onWordClick(${word.getIndex()})">
                    ${word.get()}<div class="cursor-default z-2 inline absolute -left-4 top-16" id="translation-form-${word.getIndex()}" onclick="event.stopPropagation();"></div></span>
                    @endif
                @endif
            @endfor
        </div>
        <script>
            if (!customElements.get('import-page')) {
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

                customElements.define('import-page', ImportPage);
            }
        </script>
        <import-page selectedWordId="${importPageModel.selectedWordId()}"></import-page>
    </div>
`, authenticated = authenticated, _csrf = _csrf)

