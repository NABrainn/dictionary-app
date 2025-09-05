@import lule.dictionary.translations.data.attribute.TranslationAttribute

@param TranslationAttribute translationAttribute 
@param String placeholderText
@param boolean hasError = false

<form
class="flex flex-col gap-2"
hx-params="selectedWordId,targetWord,familiarity,sourceWords"
hx-vals='{
    "targetWord": "${translationAttribute.translation().targetWord()}",
    "familiarity": "${translationAttribute.translation().familiarity()}",
    "selectedWordId": "${translationAttribute.selectedWordId()}"
}'
hx-put="/translations/sourceWords/update"
hx-target="#translation-form-container-${translationAttribute.selectedWordId()}"
hx-swap="innerHTML">
    @for(String word : translationAttribute.translation().sourceWords())
        <input
        name="sourceWords"
        type="hidden"
        value="${word}">
    @endfor
    <div class="flex gap-2">
        @if(hasError)
            <input
            id="input-source-word"
            class="
            outline-none border border-2 border-error rounded text-neutral text-md grow-1
            focus:border-accent"
            type="text"
            placeholder="${placeholderText}"
            name="sourceWords">
            <button
            id="submit-source-word"
            class="text-3xl cursor-pointer text-accent"
            type="submit">+</button>
        @else
            <input
            id="input-source-word"
            class="
            outline-none border border-2 border-primary rounded text-neutral text-md grow-1
            focus:border-accent"
            type="text"
            placeholder="${placeholderText}"
            name="sourceWords">
            <button
            id="submit-source-word"
            class="text-3xl cursor-pointer text-accent"
            type="submit">+</button>
        @endif
    </div>
</form>