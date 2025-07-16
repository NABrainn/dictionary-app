@import lule.dictionary.service.translation.dto.attribute.TranslationPair

@param TranslationPair translationPair
@param boolean hasError = false

<form
    class="flex flex-col gap-2"
    hx-vals='{
        "targetWord": "${translationPair.targetWord()}"
    }'
    hx-put="/translations/sourceWords/update"
    hx-target="#source-word-form"
    hx-swap="innerHTML">
    <div id="source-words-list">
        @template.import-page.translation.source-words-list(translationPair = translationPair)
    </div>
    <div class="flex gap-2">
        @if(hasError)
            <input
            id="input-source-word"
            class="
            outline-none border border-2 border-error rounded text-neutral text-md max-w-35
            focus:border-accent"
            type="text"
            placeholder="Add translation"
            name="sourceWords">
            <button
            id="submit-source-word"
            class="text-3xl cursor-pointer"
            type="submit">+</button>
        @else
            <input
            id="input-source-word"
            class="
            outline-none border border-2 border-primary rounded text-neutral text-md max-w-35
            focus:border-accent"
            type="text"
            placeholder="Add translation"
            name="sourceWords">
            <button
            id="submit-source-word"
            class="text-2xl cursor-pointer"
            type="submit">+</button>
        @endif
    </div>
</form>