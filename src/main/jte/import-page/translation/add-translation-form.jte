@import lule.dictionary.service.translation.dto.TranslationModel

@param TranslationModel translationModel

<script>
    function exitForm() {
        document.querySelector('.form').outerHTML = '';
    }

    if(!customElements.get('source-word-field')) {
        class SourceWordField extends HTMLElement {

            field = document.getElementById('input-source-word');

            constructor() {
                super();
            }

            isEmpty() {
                if(!this.field.value.length) {
                    this.field.classList.remove('border-primary')
                    this.field.classList.add('border-error')
                    return
                }
                this.field.classList.remove('border-error')
                this.field.classList.add('border-primary')
            }
        }
        customElements.define('source-word-field', SourceWordField);
    }
</script>
<div class="bg-secondary flex flex-col gap-2 py-2 px-6 rounded form">
    <div class="flex justify-end">
        <span class="text-red-400 font-bold text-lg cursor-pointer" onclick="exitForm()">❌</span>
    </div>
    <span class="font-bold text-lg text-accent">${translationModel.translation().targetWord()}</span>
    <div id="source-word-form" class="flex flex-col">
    @template.import-page.translation.add-source-words-form(
        targetWord = translationModel.translation().targetWord(),
        sourceLanguage = translationModel.translation().sourceLanguage().name(),
        targetLanguage = translationModel.translation().targetLanguage().name(),
        importId = translationModel.importId(),
        selectedWordId = translationModel.selectedWordId(),
        sourceWords = translationModel.translation().sourceWords(),
        page = translationModel.page()
    )
    </div>
    @template.import-page.translation.get-ai-translation-btn(targetWord = translationModel.translation().targetWord())
    <div class="flex flex-col gap-0.5">
        @for(var level : translationModel.familiarityLevels().entrySet())
            <div class="hover:text-primary font-bold text-neutral flex gap-1 cursor-pointer"
                 hx-post="/translations/new"
                 hx-vals='
                 {
                    "sourceWords": "${translationModel.translation().sourceWords().getFirst()}",
                    "targetWord": "${translationModel.translation().targetWord()}",
                    "familiarity": "${level.getValue().name()}",
                    "sourceLanguage": "${translationModel.translation().sourceLanguage().name()}",
                    "targetLanguage": "${translationModel.translation().targetLanguage().name()}",
                    "importId": "${translationModel.importId()}",
                    "selectedWordId": "${translationModel.selectedWordId()}",
                    "page": "${translationModel.page()}"
                 }'
                 hx-target="#page-content"
                 hx-swap="outerHTML">
                <span>${level.getKey()}: </span>
                ${level.getValue()}
            </div>
        @endfor
    </div>
</div>
<source-word-field></source-word-field>
