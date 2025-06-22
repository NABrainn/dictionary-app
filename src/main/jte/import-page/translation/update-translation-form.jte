@import lule.dictionary.service.translation.dto.TranslationModel

@param TranslationModel translationModel 

<script>
    function exitForm() {
        document.querySelector('.form').outerHTML = '';
    }

    if(!customElements.get('source-word-adder')) {
        class SourceWordAdder extends HTMLElement {

            sourceWords = []
            textInput = document.getElementById('input-source-word');
            vals = document.getElementById('source-word-vals');


            constructor(){
                super();
            }

            connectedCallback() {
                @for(var word : translationModel.translation().sourceWords())
                    this.sourceWords.push('${word}');
                @endfor
                this.updateVals()
                document.getElementById('submit-source-word').addEventListener('click', () => {
                    if(this.textInput.value) {
                        if(!this.sourceWords.includes(this.textInput.value)) {                            
                            if(this.sourceWords.length < 3) {
                                this.sourceWords.push(this.textInput.value)
                                this.updateUI(this.textInput.value)
                                this.updateVals()
                            }
                        }
                    }
                })

            }

            updateUI(val) {
                const sourceWordSpan = document.createElement('span')
                sourceWordSpan.classList.add('text-accent')
                sourceWordSpan.textContent = val
                document.getElementById('source-word-container').append(sourceWordSpan);
            }

            updateVals() {
                while(this.vals.firstChild) {
                    this.vals.removeChild(this.vals.firstChild)
                }

                for(const word of this.sourceWords) {
                    const val = document.createElement('input')
                    val.setAttribute("type", "hidden")
                    val.setAttribute("name", "sourceWords")
                    val.setAttribute("value", word)
                    this.vals.appendChild(val)
                }
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
        @for(String word : translationModel.translation().sourceWords())
            <span class="text-accent">${word}</span>
        @endfor
    </div>
    <div class="flex gap-2">
        <input
        id="input-source-word"
        class="
        outline-none border border-2 border-primary rounded text-neutral text-sm
        focus:border-accent"
        type="text" placeholder="Custom translation">
        <div id="source-word-vals"></div>
        <button id="submit-source-word" class="text-2xl cursor-pointer" type="button">+</button>
    </div>
    @for(var level : translationModel.familiarityLevels().entrySet())
        @if(level.getKey() == translationModel.currentFamiliarity())
            <div class="font-bold text-primary flex gap-1">
                <span>${level.getKey()}: </span>
                ${level.getValue()}
            </div>
        @else
            <div class="hover:text-primary font-bold text-neutral flex gap-1 cursor-pointer"
                 hx-put="/translations/familiarity/update"
                 hx-include="#source-word-vals"
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
        @endif
    @endfor
</div>
<source-word-adder></source-word-adder>
