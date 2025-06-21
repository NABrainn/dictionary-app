@import lule.dictionary.service.translation.dto.TranslationModel

@param TranslationModel translationModel

<script>
    function exitForm() {
        document.querySelector('.form').outerHTML = '';
    }

    if(!customElements.get('source-word-adder')) {
        class SourceWordAdder extends HTMLElement {
            sourceWord = ['${translationModel.translation().sourceWord()}']
            constructor(){
                super();
            }
            connectedCallback() {
                document.getElementById('submit-source-word').addEventListener('click', () => {
                    if(!this.sourceWord.includes(document.getElementById('input-source-word').value)) {
                        this.sourceWord.push(document.getElementById('input-source-word').value)
                        this.addToDisplay(document.getElementById('input-source-word').value)
                        document.getElementById('source-word-hidden').value = this.sourceWord.join(" ");
                    }
                })
            }

            addToDisplay(val) {
                const sourceWordSpan = document.createElement('span')
                sourceWordSpan.classList.add('text-accent')
                sourceWordSpan.textContent = val
                console.log(sourceWordSpan.innerContent)
                document.getElementById('source-word-container').append(sourceWordSpan);

            }
        }
        customElements.define('source-word-adder', SourceWordAdder)
    }
</script>
<div class="bg-secondary flex flex-col gap-1 py-2 px-6 rounded form">
    <div class="flex justify-end">
        <span class="text-red-400 font-bold text-lg cursor-pointer" onclick="exitForm()">❌</span>
    </div>
    <span class="font-bold text-lg text-accent">${translationModel.translation().targetWord()}</span>
    <div id="source-word-container" class="flex flex-col">
        <span class="text-accent">${translationModel.translation().sourceWord()}</span>
    </div>
    <div class="flex gap-2">
        <input
        id="input-source-word"
        class="
        outline-none border border-2 border-primary rounded text-neutral text-sm
        focus:border-accent"
        type="text" placeholder="Custom translation" name="sourceWord">
        <input type="hidden" name="sourceWord" id="source-word-hidden" value="${translationModel.translation().sourceWord()}">
        <button id="submit-source-word" class="text-2xl cursor-pointer" type="button">+</button>
    </div>
    @for(var level : translationModel.familiarityLevels().entrySet())
        <div class="hover:text-primary font-bold text-neutral flex gap-1 cursor-pointer"
             hx-post="/translations/new"
             hx-include="#source-word-hidden"
             hx-vals='
             {
                "targetWord": "${translationModel.translation().targetWord()}",
                "familiarity": "${level.getValue().name()}",
                "sourceLanguage": "${translationModel.translation().sourceLanguage().name()}",
                "targetLanguage": "${translationModel.translation().targetLanguage().name()}",
                "importId": "${translationModel.importId()}",
                "selectedWordId": "${translationModel.selectedWordId()}"
             }'
             hx-target="#page-content"
             hx-swap="outerHTML">
            <span>${level.getKey()}: </span>
            ${level.getValue()}
        </div>
    @endfor
</div>
<source-word-adder></source-word-adder>
